/*
 * MIT License
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.repository

import org.sopt.official.data.mapper.AuthMapper
import org.sopt.official.data.model.request.LogOutRequest
import org.sopt.official.data.model.response.LogOutResponse
import org.sopt.official.data.source.api.auth.LocalAuthDataSource
import org.sopt.official.data.source.api.auth.RemoteAuthDataSource
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.repository.AuthRepository
import org.sopt.official.network.model.request.RefreshRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val localAuthDataSource: LocalAuthDataSource,
) : AuthRepository {
    override suspend fun refresh(token: String) = runCatching {
        AuthMapper().toEntity(remoteAuthDataSource.refresh(RefreshRequest(token)))
    }

    override fun save(token: Token) {
        localAuthDataSource.save(token)
    }

    override fun save(status: UserStatus) {
        localAuthDataSource.save(status)
    }

    override suspend fun withdraw() = runCatching {
        remoteAuthDataSource.withdraw()
    }

    override suspend fun logout(
        pushToken: String
    ): Result<LogOutResponse> = runCatching {
        remoteAuthDataSource.logout(
            LogOutRequest(
                platform = "Android",
                pushToken = pushToken
            )
        )
    }

    override suspend fun clearLocalData() {
        localAuthDataSource.clear()
    }
}
