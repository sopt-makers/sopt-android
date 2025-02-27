package org.sopt.official.data.home.remote.api

import org.sopt.official.data.home.remote.response.HomeAppServiceResponseDto
import org.sopt.official.data.home.remote.response.HomeDescriptionResponseDto
import retrofit2.http.GET

internal interface HomeApi {

    @GET("home/description")
    suspend fun getHomeDescription(): HomeDescriptionResponseDto

    @GET("home/app-service")
    suspend fun getHomeAppService(): List<HomeAppServiceResponseDto>
}
