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
package org.sopt.official.stamp.domain.repository

import org.sopt.official.stamp.domain.model.User

interface UserRepository {
    suspend fun checkNickname(nickname: String)
    suspend fun checkEmail(email: String)
    suspend fun login(email: String, password: String): User
    suspend fun logout(): Result<Unit>
    suspend fun withdraw(userId: Int): Result<Unit>
    suspend fun updateProfileMessage(userId: Int, profileMessage: String): Result<Unit>
    suspend fun updatePassword(userId: Int, password: String): Result<Unit>
    suspend fun updateNickname(userId: Int, nickname: String): Result<Unit>
    fun updateLocalUserInfo(userId: Int, profileMessage: String)
    fun fetchUserId(): Int
    fun getIsOnboardingSeen(): Boolean
    fun updateOnboardingSeen(value: Boolean)
}
