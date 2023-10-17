/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.data.authenticator

import android.content.Context
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.sopt.official.common.di.Auth
import org.sopt.official.data.model.request.RefreshRequest
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.data.service.AuthService
import org.sopt.official.feature.auth.AuthActivity
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoptAuthenticator @Inject constructor(
    private val dataStore: SoptDataStore,
    @Auth(false) private val service: AuthService,
    @ApplicationContext private val context: Context
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            val refreshToken = dataStore.refreshToken
            val newTokens = runCatching {
                runBlocking {
                    service.refresh(RefreshRequest(refreshToken))
                }
            }.onSuccess {
                dataStore.refreshToken = it.refreshToken
                dataStore.accessToken = it.accessToken
                dataStore.playgroundToken = it.playgroundToken
            }.onFailure {
                dataStore.clear()
                Timber.e(it)
                ProcessPhoenix.triggerRebirth(context, AuthActivity.newInstance(context))
            }.getOrThrow()

            return response.request.newBuilder()
                .header("Authorization", newTokens.accessToken)
                .build()
        }
        return null
    }
}
