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
package org.sopt.official.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.sopt.official.network.persistence.SoptDataStore
import dev.zacsweers.metro.Inject

@Inject
class AuthInterceptor(
    private val dataStore: SoptDataStore
) : Interceptor {
    // TODO By Nunu 401 Refresh Logic 추가
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest = if (isAccessTokenUsed(originalRequest)) {
            originalRequest.newBuilder().addHeader(AUTHORIZATION, "$BEARER ${dataStore.accessToken}").build()
        } else {
            originalRequest
        }
        return chain.proceed(authRequest)
    }

    private fun isAccessTokenUsed(originalRequest: Request) = when {
        originalRequest.url.encodedPath.contains("auth/playground") -> false
        originalRequest.url.encodedPath.contains("availability") -> false
        else -> true
    }

    companion object {
        private const val BEARER = "Bearer"
        private const val AUTHORIZATION = "Authorization"
    }
}
