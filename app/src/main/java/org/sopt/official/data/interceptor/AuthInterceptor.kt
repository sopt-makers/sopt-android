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
