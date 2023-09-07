package org.sopt.official.auth.data.remote

import org.sopt.official.auth.data.remote.model.request.RequestToken
import org.sopt.official.auth.data.remote.model.response.OAuthToken
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AuthService {
    @POST("auth/playground")
    suspend fun oauth(
        @Body request: RequestToken
    ): OAuthToken
}