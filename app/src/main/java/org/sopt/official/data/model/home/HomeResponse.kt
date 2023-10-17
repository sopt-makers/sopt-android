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
package org.sopt.official.data.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.UserActiveState
import org.sopt.official.domain.entity.home.SoptActiveGeneration
import org.sopt.official.domain.entity.home.SoptActiveRecord
import org.sopt.official.domain.entity.home.SoptUser
import org.sopt.official.domain.entity.home.User

@Serializable
data class HomeResponse(
    @SerialName("user")
    val user: HomeUserResponse,
    @SerialName("operation")
    val operation: HomeOperationResponse,
    @SerialName("isAllConfirm")
    val isAllConfirm: Boolean
) {

    fun toEntity(): SoptUser = SoptUser(
        user = this.user.toEntity(),
        operation = this.operation.toEntity(),
        isAllConfirm = this.isAllConfirm
    )

    @Serializable
    data class HomeUserResponse(
        @SerialName("status")
        val status: String,
        @SerialName("name")
        val name: String?,
        @SerialName("profileImage")
        val profileImage: String?,
        @SerialName("generationList")
        val generationList: List<Long>?,
    ) {
        fun toEntity(): User = User(
            activeState = UserActiveState.valueOf(this.status),
            name = this.name,
            profileImage = this.profileImage,
            generationList = SoptActiveGeneration(this.generationList ?: listOf()),
        )
    }

    @Serializable
    data class HomeOperationResponse(
        @SerialName("attendanceScore")
        val attendanceScore: Double?,
        @SerialName("announcement")
        val announcement: String?,
    ) {
        fun toEntity(): SoptActiveRecord = SoptActiveRecord(
            attendanceScore = this.attendanceScore,
            announcement = this.announcement,
        )
    }
}
