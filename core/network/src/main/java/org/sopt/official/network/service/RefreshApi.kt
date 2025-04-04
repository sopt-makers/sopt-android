package org.sopt.official.network.service

import org.sopt.official.network.model.request.ExpiredTokenRequest
import org.sopt.official.network.model.response.NullableBaseAuthResponse
import org.sopt.official.network.model.response.ValidTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {
    @POST("/api/v1/auth/refresh/app")
    suspend fun refreshToken(
        @Body request: ExpiredTokenRequest,
    ): NullableBaseAuthResponse<ValidTokenResponse>
}