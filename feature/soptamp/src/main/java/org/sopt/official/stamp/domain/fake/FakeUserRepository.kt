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
package org.sopt.official.stamp.domain.fake

import org.sopt.official.stamp.domain.model.User
import org.sopt.official.stamp.domain.repository.UserRepository

object FakeUserRepository : UserRepository {
    private val fakeUser = User(-1, "", -1, "")
    override suspend fun signup(
        nickname: String,
        email: String,
        password: String,
        osType: String,
        clientToken: String
    ) = 1

    override suspend fun checkNickname(nickname: String) = Unit

    override suspend fun checkEmail(email: String) = Unit

    override suspend fun login(email: String, password: String) = fakeUser

    override suspend fun logout(): Result<Unit> = runCatching {}

    override suspend fun withdraw(userId: Int) = runCatching {}

    override suspend fun updateProfileMessage(userId: Int, profileMessage: String) = runCatching {}

    override suspend fun updatePassword(userId: Int, password: String) = runCatching {}

    override suspend fun updateNickname(userId: Int, nickname: String) = runCatching {}
    override fun updateLocalUserInfo(userId: Int, profileMessage: String) = Unit

    override fun fetchUserId() = 1
    override fun getIsOnboardingSeen() = false
    override fun updateOnboardingSeen(value: Boolean) = Unit
}
