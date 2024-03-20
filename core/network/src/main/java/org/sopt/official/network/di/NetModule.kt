/*
 * MIT License
 * Copyright 2022-2024 SOPT - Shout Our Passion Together
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

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.official.common.BuildConfig
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.common.di.Auth
import org.sopt.official.common.di.Logging
import org.sopt.official.common.di.OperationRetrofit
import org.sopt.official.network.FlipperInitializer
import org.sopt.official.network.authenticator.SoptAuthenticator
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetModule {

    @Logging
    @Provides
    @Singleton
    fun providerLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    @Auth
    fun provideOkHttpClient(
        @Logging loggingInterceptor: Interceptor,
        @Auth authInterceptor: Interceptor,
        authenticator: SoptAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .authenticator(authenticator)
        .apply { FlipperInitializer.addFlipperNetworkPlugin(this) }
        .build()

    @Provides
    @Singleton
    fun provideNonAuthOkHttpClient(@Logging loggingInterceptor: Interceptor,): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .apply { FlipperInitializer.addFlipperNetworkPlugin(this) }
        .build()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideKotlinSerializationConverter(json: Json): Converter.Factory = json.asConverterFactory("application/json".toMediaType())

    @AppRetrofit
    @Provides
    @Singleton
    fun provideAppRetrofit(@Auth client: OkHttpClient, converter: Converter.Factory): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(converter)
        .baseUrl(if (BuildConfig.DEBUG) BuildConfig.SOPT_DEV_BASE_URL else BuildConfig.SOPT_BASE_URL)
        .build()

    @AppRetrofit(false)
    @Provides
    @Singleton
    fun provideNoneAuthAppRetrofit(client: OkHttpClient, converter: Converter.Factory): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(converter)
        .baseUrl(if (BuildConfig.DEBUG) BuildConfig.SOPT_DEV_BASE_URL else BuildConfig.SOPT_BASE_URL)
        .build()

    @OperationRetrofit
    @Provides
    @Singleton
    fun provideOperationRetrofit(@Auth client: OkHttpClient, converter: Converter.Factory): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(converter)
        .baseUrl(if (BuildConfig.DEBUG) BuildConfig.SOPT_DEV_OPERATION_BASE_URL else BuildConfig.SOPT_OPERATION_BASE_URL)
        .build()
}
