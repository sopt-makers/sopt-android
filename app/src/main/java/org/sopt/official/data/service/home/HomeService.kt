package org.sopt.official.data.service.home

import org.sopt.official.data.model.home.DescriptionViewResponse
import org.sopt.official.data.model.home.HomeResponse
import retrofit2.http.GET

interface HomeService {
    @GET("user/main")
    suspend fun getMainView(): HomeResponse

    @GET("description/main")
    suspend fun getMainDescription(): DescriptionViewResponse
}