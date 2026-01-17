/*
 * MIT License
 * Copyright 2022-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.network.di

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.official.common.BuildConfig
import org.sopt.official.common.di.AppRetrofitInstance
import org.sopt.official.common.di.AppScope
import org.sopt.official.common.di.Auth
import org.sopt.official.common.di.AuthOkHttpClient
import org.sopt.official.common.di.AuthRetrofitInstance
import org.sopt.official.common.di.Logging
import org.sopt.official.common.di.LoggingInterceptor
import org.sopt.official.common.di.NonAuthOkHttpClient
import org.sopt.official.common.di.NoneAuthRetrofitInstance
import org.sopt.official.common.di.OperationRetrofitInstance
import org.sopt.official.network.authenticator.CentralizeAuthenticator
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@ContributesTo(AppScope::class)
@BindingContainer
interface NetModule {

    companion object {
        @Logging
        @Provides
        fun providerLoggingInterceptor(): LoggingInterceptor = LoggingInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
        )

        @Provides
        fun provideOkHttpClient(
            @Logging loggingInterceptor: LoggingInterceptor,
            @Auth authInterceptor: Interceptor,
            authenticator: CentralizeAuthenticator
        ): AuthOkHttpClient = AuthOkHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor.interceptor)
                .addInterceptor(authInterceptor)
                .authenticator(authenticator)
                .build()
        )

        @Provides
        fun provideNonAuthOkHttpClient(@Logging loggingInterceptor: LoggingInterceptor): NonAuthOkHttpClient = NonAuthOkHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor.interceptor)
                .build()
        )

        @Provides
        fun provideKotlinSerializationConverter(json: Json): Factory = json.asConverterFactory("application/json".toMediaType())

        @Provides
        fun provideAppRetrofit(client: AuthOkHttpClient, converter: Factory): AppRetrofitInstance = AppRetrofitInstance(
            Retrofit.Builder()
                .client(client.client)
                .addConverterFactory(converter)
                .baseUrl(if (BuildConfig.DEBUG) BuildConfig.SOPT_DEV_BASE_URL else BuildConfig.SOPT_BASE_URL)
                .build()
        )

        @Provides
        fun provideNoneAuthAppRetrofit(client: NonAuthOkHttpClient, converter: Factory): NoneAuthRetrofitInstance = NoneAuthRetrofitInstance(
            Retrofit.Builder()
                .client(client.client)
                .addConverterFactory(converter)
                .baseUrl(if (BuildConfig.DEBUG) BuildConfig.SOPT_DEV_BASE_URL else BuildConfig.SOPT_BASE_URL)
                .build()
        )

        @Provides
        fun provideOperationRetrofit(client: AuthOkHttpClient, converter: Factory): OperationRetrofitInstance = OperationRetrofitInstance(
            Retrofit.Builder()
                .client(client.client)
                .addConverterFactory(converter)
                .baseUrl(if (BuildConfig.DEBUG) BuildConfig.SOPT_DEV_OPERATION_BASE_URL else BuildConfig.SOPT_OPERATION_BASE_URL)
                .build()
        )

        @Provides
        fun provideAuthRetrofit(client: AuthOkHttpClient, converter: Factory): AuthRetrofitInstance = AuthRetrofitInstance(
            Retrofit.Builder()
                .client(client.client)
                .addConverterFactory(converter)
                .baseUrl(if (BuildConfig.DEBUG) BuildConfig.DEV_AUTH_API else BuildConfig.PROD_AUTH_API)
                .build()
        )
    }
}