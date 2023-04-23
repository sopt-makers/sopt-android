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

import org.sopt.official.stamp.data.remote.api.RankService
import org.sopt.official.stamp.data.remote.api.SoptampUserService
import org.sopt.official.stamp.data.remote.model.request.UpdateNicknameRequest
import org.sopt.official.stamp.data.remote.model.request.UpdateProfileMessageRequest
import org.sopt.official.stamp.data.remote.model.response.UserResponse
import org.sopt.official.stamp.data.source.UserDataSource
import javax.inject.Inject

internal class RemoteUserDataSource @Inject constructor(
    private val soptampUserService: SoptampUserService,
    private val rankService: RankService
) : UserDataSource {

    override suspend fun checkNickname(nickname: String) {
        return soptampUserService.checkNickname(nickname)
    }

    override suspend fun updateNickname(new: String) {
        soptampUserService.updateNickname(UpdateNicknameRequest(new))
    }

    override suspend fun updateProfileMessage(new: String) {
        rankService.updateProfileMessage(UpdateProfileMessageRequest(new))
    }

    override suspend fun getUserInfo(): UserResponse {
        return soptampUserService.getUserInformation()
    }
}
