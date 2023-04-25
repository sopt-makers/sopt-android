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
import org.sopt.official.core.di.AppRetrofit
import org.sopt.official.core.di.Auth
import org.sopt.official.core.di.Logging
import org.sopt.official.core.di.OperationRetrofit
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
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Logging loggingInterceptor: Interceptor,
        @Auth authInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
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
        client: OkHttpClient,
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
        client: OkHttpClient,
        converter: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(converter)
        .baseUrl(if (BuildConfig.DEBUG) BuildConfig.devOperationApi else BuildConfig.operationApi)
        .build()
}
