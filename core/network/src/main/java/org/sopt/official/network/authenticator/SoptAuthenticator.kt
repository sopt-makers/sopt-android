/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.network.authenticator

import android.app.Application
import com.jakewharton.processphoenix.ProcessPhoenix
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.sopt.official.common.di.AppScope
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.network.model.request.RefreshRequest
import org.sopt.official.network.persistence.SoptDataStore
import org.sopt.official.network.service.RefreshService
import timber.log.Timber

@Deprecated("이거 말고 CentralizeAuthenticator 사용하세요.")
@SingleIn(AppScope::class)
@Inject
class SoptAuthenticator(
    private val dataStore: SoptDataStore,
    private val refreshService: RefreshService,
    private val application: Application,
    private val navigatorProvider: NavigatorProvider
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            val refreshToken = dataStore.refreshToken
            if (refreshToken.isEmpty()) return null
            val newTokens = runCatching {
                runBlocking {
                    refreshService.refresh(RefreshRequest(refreshToken))
                }
            }.onSuccess {
                dataStore.refreshToken = it.refreshToken
                dataStore.accessToken = it.accessToken
                dataStore.playgroundToken = it.playgroundToken
            }.onFailure {
                dataStore.clear()
                Timber.e(it)
                ProcessPhoenix.triggerRebirth(application.applicationContext, navigatorProvider.getAuthActivityIntent())
            }.getOrThrow()

            return response.request.newBuilder()
                .header("Authorization", newTokens.accessToken)
                .build()
        }
        return null
    }
}