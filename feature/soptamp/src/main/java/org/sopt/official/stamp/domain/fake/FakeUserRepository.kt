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

import org.sopt.official.stamp.domain.model.SoptampUser
import org.sopt.official.stamp.domain.repository.UserRepository

object FakeUserRepository : UserRepository {
    private val fakeUser = SoptampUser("", 1, "")

    override suspend fun checkNickname(nickname: String) = Unit

    override suspend fun logout(): Result<Unit> = runCatching {}
    override suspend fun getUserInfo(): Result<SoptampUser> = runCatching { fakeUser }

    override suspend fun updateProfileMessage(profileMessage: String): Result<Unit> = runCatching {}

    override suspend fun updateNickname(nickname: String): Result<Unit> = runCatching {}
    override fun updateLocalUserInfo(profileMessage: String) = Unit

    override fun fetchUserId() = 1
    override fun getIsOnboardingSeen() = false
    override fun updateOnboardingSeen(value: Boolean) = Unit
}
