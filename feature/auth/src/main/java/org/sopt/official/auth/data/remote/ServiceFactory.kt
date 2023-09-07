package org.sopt.official.auth.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.official.auth.utils.PlaygroundHttpLoggingInterceptor
import retrofit2.Retrofit

internal object ServiceFactory {
    @OptIn(ExperimentalSerializationApi::class)
    fun withClient(
        url: String,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(client)
        .addConverterFactory(serializer.asConverterFactory("application/json".toMediaType()))
        .build()

    private val serializer by lazy {
        Json {
            prettyPrint = true
            isLenient = true
        }
    }

    val loggingInterceptor by lazy {
        HttpLoggingInterceptor(PlaygroundHttpLoggingInterceptor()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}