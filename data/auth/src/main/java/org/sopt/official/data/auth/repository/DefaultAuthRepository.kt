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

import dev.zacsweers.metro.Inject
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.auth.mapper.toAuthDomain
import org.sopt.official.data.auth.mapper.toCertificateCodeRequest
import org.sopt.official.data.auth.mapper.toChangeAccountRequest
import org.sopt.official.data.auth.mapper.toCreateCodeRequest
import org.sopt.official.data.auth.mapper.toSignInRequest
import org.sopt.official.data.auth.mapper.toSignUpRequest
import org.sopt.official.data.auth.mapper.toUserDomain
import org.sopt.official.data.auth.remote.api.AuthApi
import org.sopt.official.domain.auth.model.Auth
import org.sopt.official.domain.auth.model.User
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.network.persistence.SoptDataStore

@Inject
class DefaultAuthRepository(
    private val authApi: AuthApi,
    private val soptDataStore: SoptDataStore,
) : AuthRepository {
    override suspend fun createCode(request: User): Result<Unit> = suspendRunCatching {
        authApi.createCode(request.toCreateCodeRequest())
    }

    override suspend fun certificateCode(request: User): Result<User> = suspendRunCatching {
        authApi.certificateCode(request.toCertificateCodeRequest()).data.toUserDomain()
    }

    override suspend fun signIn(request: Auth): Result<Auth> = suspendRunCatching {
        authApi.signIn(request.toSignInRequest()).data.toAuthDomain()
    }

    override suspend fun signUp(
        userRequest: User,
        authRequest: Auth
    ): Result<Unit> = suspendRunCatching {
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
    ): Result<Unit> = suspendRunCatching {
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
    ): Result<Auth> = suspendRunCatching {
        authApi.findAccount(
            name = name,
            phone = phone
        ).data.toAuthDomain()
    }

    override suspend fun saveUserToken(
        accessToken: String,
        refreshToken: String
    ) {
        soptDataStore.accessToken = accessToken
        soptDataStore.refreshToken = refreshToken
    }

    override suspend fun clearUserToken() {
        soptDataStore.clear()
    }
}
