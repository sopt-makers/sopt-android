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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.sopt.official.common.di.AppScope
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.network.model.request.ExpiredTokenRequest
import org.sopt.official.network.persistence.SoptDataStore
import org.sopt.official.network.service.RefreshApi
import timber.log.Timber

@SingleIn(AppScope::class)
class CentralizeAuthenticator @Inject constructor(
    private val dataStore: SoptDataStore,
    private val refreshApi: RefreshApi,
    private val application: Application,
    private val navigatorProvider: NavigatorProvider
) : Authenticator {
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            handleAuthentication(response)
        }
    }

    private suspend fun handleAuthentication(response: Response): Request? = mutex.withLock {
        val requestToken = getRequestToken(response.request)

        // 다른 스레드에서 이미 토큰이 갱신된 경우 -> 갱신된 토큰으로 재요청
        val currentAccessToken = dataStore.accessToken
        if (isTokenRefreshed(currentAccessToken, requestToken)) {
            Timber.d("토큰 이미 갱신 완료. 새 토큰으로 재시도: $BEARER $currentAccessToken")
            return@withLock buildRequestWithToken(response.request, currentAccessToken)
        }

        // refreshToken이 없는 경우
        val refreshToken = dataStore.refreshToken

        if (refreshToken.isBlank()) {
            Timber.d("리프레시 토큰 없어 재로그인 필요 -> 앱 재시작")
            ProcessPhoenix.triggerRebirth(application.applicationContext, navigatorProvider.getAuthActivityIntent())
            return@withLock null
        }

        // 토큰 재발급 시작
        val newTokens = runCatching {
            refreshApi.refreshToken(
                ExpiredTokenRequest(
                    accessToken = "$BEARER $currentAccessToken",
                    refreshToken = refreshToken
                )
            )
        }.onSuccess {
            Timber.d("토큰 재발급 성공. 요청 재시도.")
            dataStore.accessToken = it.data?.accessToken.orEmpty()
            dataStore.refreshToken = it.data?.refreshToken.orEmpty()
        }.onFailure {
            Timber.e(it, "토큰 재발급 실패. 토큰 삭제 및 앱 재시작.")
            dataStore.clear()
            ProcessPhoenix.triggerRebirth(application.applicationContext, navigatorProvider.getAuthActivityIntent())
        }.getOrThrow()

        return buildRequestWithToken(response.request, newTokens.data?.accessToken.orEmpty())
    }

    private fun getRequestToken(request: Request): String? =
        request.header(AUTHORIZATION)?.removePrefix(BEARER)?.trim()

    private fun isTokenRefreshed(currentToken: String, requestToken: String?): Boolean {
        if (currentToken.isBlank()) return false

        return requestToken != currentToken
    }

    private fun buildRequestWithToken(request: Request, token: String): Request =
        request.newBuilder()
            .removeHeader(AUTHORIZATION)
            .addHeader(AUTHORIZATION, "$BEARER $token")
            .build()

    companion object {
        private const val BEARER = "Bearer"
        private const val AUTHORIZATION = "Authorization"
    }
}