package org.sopt.official.auth.data

import okhttp3.OkHttpClient
import org.sopt.official.auth.Constants
import org.sopt.official.auth.PlaygroundInfo
import org.sopt.official.auth.data.remote.AuthService
import org.sopt.official.auth.data.remote.ServiceFactory
import retrofit2.Retrofit

internal class PlaygroundApiManager private constructor(
    private val retrofit: Retrofit
) {
    fun provideAuthService(): AuthService = retrofit.create(AuthService::class.java)

    companion object {
        @Volatile
        private var instance: PlaygroundApiManager? = null

        fun getInstance(isDebug: Boolean) = instance ?: synchronized(this) {
            instance ?: PlaygroundApiManager(createRetrofit(isDebug)).also {
                instance = it
            }
        }

        private fun createRetrofit(isDebug: Boolean) = ServiceFactory.withClient(
            url = "${Constants.SCHEME}://${if (isDebug) PlaygroundInfo.DEBUG_AUTH_HOST else PlaygroundInfo.RELEASE_AUTH_HOST}",
            client = OkHttpClient.Builder()
                .addInterceptor(ServiceFactory.loggingInterceptor)
                .build()
        )
    }
}