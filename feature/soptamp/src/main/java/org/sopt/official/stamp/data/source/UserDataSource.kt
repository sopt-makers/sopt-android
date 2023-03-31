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
package org.sopt.official.stamp.data.source

import org.sopt.official.stamp.data.remote.model.response.SignUpResponse
import org.sopt.official.stamp.data.remote.model.response.UserResponse

interface UserDataSource {
    suspend fun signup(nickname: String, email: String, password: String, osType: String, clientToken: String): SignUpResponse
    suspend fun checkNickname(nickname: String)
    suspend fun checkEmail(email: String)
    suspend fun login(email: String, password: String): UserResponse
    suspend fun withdraw(userId: Int)
    suspend fun updatePassword(userId: Int, new: String)
    suspend fun updateNickname(userId: Int, new: String)
    suspend fun updateProfileMessage(userId: Int, new: String)
}
