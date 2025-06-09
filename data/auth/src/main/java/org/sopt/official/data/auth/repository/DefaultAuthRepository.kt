/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.auth.repository

import org.sopt.official.data.auth.mapper.toCertificateCodeRequest
import org.sopt.official.data.auth.mapper.toChangeAccountRequest
import org.sopt.official.data.auth.mapper.toCreateCodeRequest
import org.sopt.official.data.auth.mapper.toDomain
import org.sopt.official.data.auth.mapper.toSignInRequest
import org.sopt.official.data.auth.mapper.toSignUpRequest
import org.sopt.official.data.auth.remote.api.AuthApi
import org.sopt.official.domain.auth.model.AccountResult
import org.sopt.official.domain.auth.model.Auth
import org.sopt.official.domain.auth.model.Token
import org.sopt.official.domain.auth.model.User
import org.sopt.official.domain.auth.model.VerificationResult
import org.sopt.official.domain.auth.repository.AuthRepository
import javax.inject.Inject

internal class DefaultAuthRepository @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun createCode(request: User): Result<Unit> = runCatching {
        authApi.createCode(request.toCreateCodeRequest())
    }

    override suspend fun certificateCode(request: User): Result<VerificationResult> = runCatching {
        authApi.certificateCode(request.toCertificateCodeRequest()).data.toDomain()
    }

    override suspend fun signIn(request: Auth): Result<Token> = runCatching {
        authApi.signIn(request.toSignInRequest()).data.toDomain()
    }

    override suspend fun signUp(
        userRequest: User,
        authRequest: Auth
    ): Result<Unit> = runCatching {
        authApi.signUp(
            toSignUpRequest(
                user = userRequest,
                auth = authRequest
            )
        )
    }

    override suspend fun changeAccount(
        userRequest: User,
        authRequest: Auth,
    ): Result<Unit> = runCatching {
        authApi.changeAccount(
            toChangeAccountRequest(
                user = userRequest,
                auth = authRequest
            )
        )
    }

    override suspend fun findAccount(
        name: String,
        phone: String,
    ): Result<AccountResult> = runCatching {
        authApi.findAccount(
            name = name,
            phone = phone
        ).data.toDomain()
    }
}
