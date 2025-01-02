package org.sopt.official.data.auth.remote.api

import org.sopt.official.data.auth.model.BaseAuthResponse
import org.sopt.official.data.auth.model.NonDataBaseAuthResponse
import org.sopt.official.data.auth.remote.request.CertificateCodeRequest
import org.sopt.official.data.auth.remote.request.CreateCodeRequest
import org.sopt.official.data.auth.remote.request.SignInRequest
import org.sopt.official.data.auth.remote.response.CertificateCodeResponse
import org.sopt.official.data.auth.remote.response.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AuthApi {
    @POST("/api/v1/auth/phone")
    suspend fun createCode(
        @Body request: CreateCodeRequest,
    ): NonDataBaseAuthResponse

    @POST("/api/v1/auth/verify/phone")
    suspend fun certificateCode(
        @Body request: CertificateCodeRequest,
    ): BaseAuthResponse<CertificateCodeResponse>

    @POST("/api/v1/auth/login/app")
    suspend fun signIn(
        @Body request: SignInRequest,
    ): BaseAuthResponse<SignInResponse>
}