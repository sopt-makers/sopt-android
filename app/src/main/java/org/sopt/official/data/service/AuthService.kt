package org.sopt.official.data.service

import org.sopt.official.data.model.request.AuthRequest
import org.sopt.official.data.model.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/playground")
    suspend fun authenticate(
        @Body body: AuthRequest
    ): AuthResponse
}
