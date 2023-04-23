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

import org.sopt.official.stamp.domain.model.SoptampUser

interface UserRepository {
    suspend fun checkNickname(nickname: String)
    suspend fun logout(): Result<Unit>
    suspend fun getUserInfo(): Result<SoptampUser>
    suspend fun updateProfileMessage(profileMessage: String): Result<Unit>
    suspend fun updateNickname(nickname: String): Result<Unit>
    fun updateLocalUserInfo(profileMessage: String)
    fun fetchUserId(): Int
    fun getIsOnboardingSeen(): Boolean
    fun updateOnboardingSeen(value: Boolean)
}
