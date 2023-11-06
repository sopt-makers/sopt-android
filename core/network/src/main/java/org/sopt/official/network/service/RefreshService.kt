package org.sopt.official.network.service

import org.sopt.official.network.model.response.AuthResponse
import org.sopt.official.network.model.request.RefreshRequest
import retrofit2.http.Body
import retrofit2.http.PATCH

interface RefreshService {
    @PATCH("auth/refresh")
    suspend fun refresh(
        @Body body: RefreshRequest
    ): AuthResponse

}