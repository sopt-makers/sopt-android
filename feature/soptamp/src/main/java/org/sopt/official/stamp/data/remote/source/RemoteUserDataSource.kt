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
package org.sopt.official.stamp.data.remote.source

import org.sopt.official.stamp.data.remote.model.request.LoginRequest
import org.sopt.official.stamp.data.remote.model.request.SignUpRequest
import org.sopt.official.stamp.data.remote.model.request.UpdateNicknameRequest
import org.sopt.official.stamp.data.remote.model.request.UpdatePasswordRequest
import org.sopt.official.stamp.data.remote.model.request.UpdateProfileMessageRequest
import org.sopt.official.stamp.data.remote.model.response.SignUpResponse
import org.sopt.official.stamp.data.remote.api.RankService
import org.sopt.official.stamp.data.remote.api.UserService
import org.sopt.official.stamp.data.remote.model.response.UserResponse
import org.sopt.official.stamp.data.source.UserDataSource
import retrofit2.HttpException
import javax.inject.Inject

internal class RemoteUserDataSource @Inject constructor(
    private val userService: UserService,
    private val rankService: RankService
) : UserDataSource {
    override suspend fun signup(
        nickname: String,
        email: String,
        password: String,
        osType: String,
        clientToken: String
    ): SignUpResponse {
        return userService.signup(
            SignUpRequest(
                nickname,
                email,
                password
            )
        )
    }

    override suspend fun checkNickname(nickname: String) {
        return userService.checkNickname(nickname)
    }

    override suspend fun checkEmail(email: String) = userService.checkEmail(email)

    override suspend fun login(email: String, password: String): UserResponse {
        val response = userService.login(
            LoginRequest(email, password)
        )
        return if (response.isSuccessful) {
            UserResponse(
                response.body()?.userId,
                response.body()?.message,
                response.code()
            )
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun withdraw(userId: Int) {
        userService.withdraw(userId)
    }

    override suspend fun updatePassword(userId: Int, new: String) {
        userService.updatePassword(userId, UpdatePasswordRequest(new))
    }

    override suspend fun updateNickname(userId: Int, new: String) {
        userService.updateNickname(userId, UpdateNicknameRequest(new))
    }

    override suspend fun updateProfileMessage(userId: Int, new: String) {
        rankService.updateProfileMessage(userId, UpdateProfileMessageRequest(new))
    }
}
