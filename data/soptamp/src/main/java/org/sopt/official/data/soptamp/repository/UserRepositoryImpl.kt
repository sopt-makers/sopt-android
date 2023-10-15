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
package org.sopt.official.data.soptamp.repository

import org.sopt.official.data.soptamp.local.SoptampDataStore
import org.sopt.official.data.soptamp.source.UserDataSource
import org.sopt.official.domain.soptamp.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remote: UserDataSource,
    private val local: SoptampDataStore
) : UserRepository {
    override val nickname: String
        get() = local.nickname

    override suspend fun checkNickname(nickname: String) = runCatching { remote.checkNickname(nickname) }
    override suspend fun logout(): Result<Unit> = runCatching { local.clear() }

    override suspend fun getUserInfo() = runCatching {
        remote.getUserInfo().toDomain()
    }.onSuccess {
        local.nickname = it.nickname
        local.profileMessage = it.profileMessage
    }

    override suspend fun updateProfileMessage(profileMessage: String) = runCatching {
        remote.updateProfileMessage(profileMessage)
    }.onSuccess {
        local.profileMessage = profileMessage
    }

    override suspend fun updateNickname(nickname: String): Result<Unit> = runCatching {
        remote.updateNickname(nickname)
    }.onSuccess {
        local.nickname = nickname
    }

    override fun updateLocalUserInfo(profileMessage: String) {
        local.apply {
            this.profileMessage = profileMessage
        }
    }

    override fun fetchUserId() = local.userId
    override fun getIsOnboardingSeen() = local.isOnboardingSeen
    override fun updateOnboardingSeen(value: Boolean) {
        local.isOnboardingSeen = value
    }
}
