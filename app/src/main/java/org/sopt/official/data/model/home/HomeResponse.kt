/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
