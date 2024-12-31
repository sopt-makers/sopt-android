package org.sopt.official.data.auth.remote.api

import org.sopt.official.data.auth.remote.request.CertificationNumberRequest
import org.sopt.official.data.auth.remote.response.CertificationNumberResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AuthApi {
    @POST("/api/v1/auth/phone")
    suspend fun postCertificationNumber(
        @Body request: CertificationNumberRequest,
    ): CertificationNumberResponse
}