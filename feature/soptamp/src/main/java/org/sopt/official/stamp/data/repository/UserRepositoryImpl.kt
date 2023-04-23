/*
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.data.repository

import org.sopt.official.stamp.data.local.SoptampDataStore
import org.sopt.official.stamp.data.source.UserDataSource
import org.sopt.official.stamp.domain.model.User
import org.sopt.official.stamp.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remote: UserDataSource,
    private val local: SoptampDataStore
) : UserRepository {
    override suspend fun checkNickname(nickname: String) = remote.checkNickname(nickname)

    override suspend fun login(email: String, password: String): User = remote.login(email, password).toUser()
    override suspend fun logout(): Result<Unit> = runCatching { local.clear() }

    override suspend fun withdraw(userId: Int): Result<Unit> = runCatching {
        remote.withdraw(userId)
    }.onSuccess {
        local.clear()
    }

    override suspend fun updateProfileMessage(
        userId: Int,
        profileMessage: String
    ): Result<Unit> = runCatching {
        remote.updateProfileMessage(userId, profileMessage)
    }.onSuccess {
        local.profileMessage = profileMessage
    }

    override suspend fun updatePassword(
        userId: Int,
        password: String
    ): Result<Unit> = runCatching {
        remote.updatePassword(userId, password)
    }

    override suspend fun updateNickname(
        userId: Int,
        nickname: String
    ): Result<Unit> = runCatching {
        remote.updateNickname(userId, nickname)
    }.onSuccess {
        local.nickname = nickname
    }

    override fun updateLocalUserInfo(userId: Int, profileMessage: String) {
        local.apply {
            this.userId = userId
            this.profileMessage = profileMessage
        }
    }

    override fun fetchUserId() = local.userId
    override fun getIsOnboardingSeen() = local.isOnboardingSeen
    override fun updateOnboardingSeen(value: Boolean) {
        local.isOnboardingSeen = value
    }
}
