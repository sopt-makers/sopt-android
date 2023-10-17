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
package org.sopt.official.data.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.sopt.official.data.persistence.SoptDataStore
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStore: SoptDataStore
) : Interceptor {
    // TODO By Nunu 401 Refresh Logic 추가
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest = if (isAccessTokenUsed(originalRequest)) {
            originalRequest.newBuilder().addHeader("Authorization", dataStore.accessToken).build()
        } else {
            originalRequest
        }
        return chain.proceed(authRequest)
    }

    private fun isAccessTokenUsed(originalRequest: Request) =
        when {
            originalRequest.url.encodedPath.contains("playground") -> false
            originalRequest.url.encodedPath.contains("availability") -> false
            else -> true
        }
}
