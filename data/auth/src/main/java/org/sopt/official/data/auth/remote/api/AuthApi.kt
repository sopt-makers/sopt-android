/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.data.auth.remote.api

import org.sopt.official.data.auth.model.BaseAuthResponse
import org.sopt.official.data.auth.model.NullableBaseAuthResponse
import org.sopt.official.data.auth.remote.request.CertificateCodeRequest
import org.sopt.official.data.auth.remote.request.ChangeAccountRequest
import org.sopt.official.data.auth.remote.request.CreateCodeRequest
import org.sopt.official.data.auth.remote.request.SignInRequest
import org.sopt.official.data.auth.remote.request.SignUpRequest
import org.sopt.official.data.auth.remote.response.CertificateCodeResponse
import org.sopt.official.data.auth.remote.response.FindAccountResponse
import org.sopt.official.data.auth.remote.response.SignInResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("/api/v1/auth/phone")
    suspend fun createCode(
        @Body request: CreateCodeRequest,
    ): NullableBaseAuthResponse<Unit>

    @POST("/api/v1/auth/verify/phone")
    suspend fun certificateCode(
        @Body request: CertificateCodeRequest,
    ): BaseAuthResponse<CertificateCodeResponse>

    @POST("/api/v1/auth/login/app")
    suspend fun signIn(
        @Body request: SignInRequest,
    ): BaseAuthResponse<SignInResponse>

    @POST("/api/v1/auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest,
    ): NullableBaseAuthResponse<Unit>

    @PATCH("/api/v1/social/accounts")
    suspend fun changeAccount(
        @Body request: ChangeAccountRequest
    ): NullableBaseAuthResponse<Unit>

    @GET("/api/v1/social/accounts/platform")
    suspend fun findAccount(
        @Query("name") name: String,
        @Query("phone") phone: String
    ): BaseAuthResponse<FindAccountResponse>
}
