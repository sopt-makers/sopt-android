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
package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.model.OtherMission

@Serializable
data class RankDetailResponse(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileMessage")
    val profileMessage: String?,
    @SerialName("userMissions")
    val userMissions: List<UserMissionResponse>
) {
    @Serializable
    data class UserMissionResponse(
        @SerialName("display")
        val display: Boolean,
        @SerialName("id")
        val id: Int,
        @SerialName("level")
        val level: Int,
        @SerialName("profileImage")
        val profileImage: String?,
        @SerialName("title")
        val title: String
    ) {
        fun toEntity() = OtherMission.Mission(
            display = display,
            id = id,
            level = level,
            profileImage = profileImage,
            title = title,
        )
    }

    fun toEntity() = OtherMission(
        nickname = nickname,
        profileMessage = profileMessage ?: "",
        userMissions = userMissions.map { it.toEntity() }
    )
}
