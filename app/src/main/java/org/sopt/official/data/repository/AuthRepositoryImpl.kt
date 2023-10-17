/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.data.repository

import org.sopt.official.data.model.request.LogOutRequest
import org.sopt.official.data.model.request.RefreshRequest
import org.sopt.official.data.model.response.LogOutResponse
import org.sopt.official.data.source.api.auth.LocalAuthDataSource
import org.sopt.official.data.source.api.auth.RemoteAuthDataSource
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val localAuthDataSource: LocalAuthDataSource,
) : AuthRepository {
    override suspend fun refresh(token: String) = runCatching {
        remoteAuthDataSource.refresh(RefreshRequest(token)).toEntity()
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
