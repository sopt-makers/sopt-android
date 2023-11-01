package org.sopt.official.network.service

import org.sopt.official.network.model.AuthRequest
import org.sopt.official.network.model.AuthResponse
import org.sopt.official.network.model.RefreshRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthService {
    @PATCH("auth/refresh")
    suspend fun refresh(
        @Body body: RefreshRequest
    ): AuthResponse

}