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
