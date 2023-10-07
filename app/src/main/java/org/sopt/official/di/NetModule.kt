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
    @Auth(true)
    fun provideOkHttpClient(
        @Logging loggingInterceptor: Interceptor,
        @Auth(true) authInterceptor: Interceptor,
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

    @AppRetrofit(true)
    @Provides
    @Singleton
    fun provideAppRetrofit(
        @Auth(true) client: OkHttpClient,
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
