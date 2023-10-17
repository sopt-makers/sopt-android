/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.official.BuildConfig
import org.sopt.official.FlipperInitializer
import org.sopt.official.common.di.AppRetrofit
import org.sopt.official.common.di.Auth
import org.sopt.official.common.di.Logging
import org.sopt.official.common.di.OperationRetrofit
import org.sopt.official.data.authenticator.SoptAuthenticator
import retrofit2.Converter
import retrofit2.Retrofit
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
    @Auth(false)
    fun provideNonAuthOkHttpClient(
        @Logging loggingInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
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
    fun provideKotlinSerializationConverter(json: Json): Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())

    @AppRetrofit
    @Provides
    @Singleton
    fun provideAppRetrofit(
        @Auth client: OkHttpClient,
        converter: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(converter)
        .baseUrl(if (BuildConfig.DEBUG) BuildConfig.devApi else BuildConfig.newApi)
        .build()

    @AppRetrofit(false)
    @Provides
    @Singleton
    fun provideNoneAuthAppRetrofit(
        @Auth(false) client: OkHttpClient,
        converter: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(converter)
        .baseUrl(if (BuildConfig.DEBUG) BuildConfig.devApi else BuildConfig.newApi)
        .build()

    @OperationRetrofit
    @Provides
    @Singleton
    fun provideOperationRetrofit(
        @Auth client: OkHttpClient,
        converter: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(converter)
        .baseUrl(if (BuildConfig.DEBUG) BuildConfig.devOperationApi else BuildConfig.operationApi)
        .build()
}
