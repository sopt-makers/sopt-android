package org.sopt.official.data.service

import org.sopt.official.data.model.request.AuthRequest
import org.sopt.official.data.model.request.LogOutRequest
import org.sopt.official.data.model.request.RefreshRequest
import org.sopt.official.data.model.response.AuthResponse
import org.sopt.official.data.model.response.LogOutResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthService {
    @POST("auth/playground")
    suspend fun authenticate(
        @Body body: AuthRequest
    ): AuthResponse

    @PATCH("auth/refresh")
    suspend fun refresh(
        @Body body: RefreshRequest
    ): AuthResponse

    @DELETE("user")
    suspend fun withdraw()

    @HTTP(method = "DELETE", path = "user/logout", hasBody = true)
    suspend fun logOut(
        @Body body: LogOutRequest
    ): LogOutResponse
}
