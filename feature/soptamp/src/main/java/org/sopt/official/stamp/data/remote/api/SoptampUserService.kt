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
package org.sopt.official.stamp.data.remote.api

import org.sopt.official.stamp.data.remote.model.request.UpdateNicknameRequest
import org.sopt.official.stamp.data.remote.model.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface SoptampUserService {
    // 닉네임 중복검사
    @GET("user/nickname/{nickname}")
    suspend fun checkNickname(@Path("nickname") nickname: String)

    @GET("user/soptamp")
    suspend fun getUserInformation(): UserResponse

    // 닉네임 변경
    @PATCH("auth/nickname")
    suspend fun updateNickname(@Body nickname: UpdateNicknameRequest)

    // 탈퇴하기
    @DELETE("auth/withdraw")
    suspend fun withdraw()
}
