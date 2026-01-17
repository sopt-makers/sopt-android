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
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.official.common.BuildConfig
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.common.di.AppScope
import org.sopt.official.common.di.Auth
import org.sopt.official.common.di.AuthRetrofit
import org.sopt.official.common.di.Logging
import org.sopt.official.common.di.OperationRetrofit
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
        @SingleIn(AppScope::class)
        fun providerLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        @Provides
        @SingleIn(AppScope::class)
        @Auth
        fun provideOkHttpClient(
            @Logging loggingInterceptor: Interceptor,
            @Auth authInterceptor: Interceptor,
            authenticator: CentralizeAuthenticator
        ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .build()

        @Provides
        @SingleIn(AppScope::class)
        fun provideNonAuthOkHttpClient(@Logging loggingInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        @Provides
        @SingleIn(AppScope::class)
        fun provideKotlinSerializationConverter(json: Json): Factory = json.asConverterFactory("application/json".toMediaType())

        @AppRetrofit
        @Provides
        @SingleIn(AppScope::class)
        fun provideAppRetrofit(@Auth client: OkHttpClient, converter: Factory): Retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(converter)
            .baseUrl(if (BuildConfig.DEBUG) BuildConfig.SOPT_DEV_BASE_URL else BuildConfig.SOPT_BASE_URL)
            .build()

        @AppRetrofit(false)
        @Provides
        @SingleIn(AppScope::class)
        fun provideNoneAuthAppRetrofit(client: OkHttpClient, converter: Factory): Retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(converter)
            .baseUrl(if (BuildConfig.DEBUG) BuildConfig.SOPT_DEV_BASE_URL else BuildConfig.SOPT_BASE_URL)
            .build()

        @OperationRetrofit
        @Provides
        @SingleIn(AppScope::class)
        fun provideOperationRetrofit(@Auth client: OkHttpClient, converter: Factory): Retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(converter)
            .baseUrl(if (BuildConfig.DEBUG) BuildConfig.SOPT_DEV_OPERATION_BASE_URL else BuildConfig.SOPT_OPERATION_BASE_URL)
            .build()

        @AuthRetrofit
        @Provides
        @SingleIn(AppScope::class)
        fun provideAuthRetrofit(client: OkHttpClient, converter: Factory): Retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(converter)
            .baseUrl(if (BuildConfig.DEBUG) BuildConfig.DEV_AUTH_API else BuildConfig.PROD_AUTH_API)
            .build()
    }
}