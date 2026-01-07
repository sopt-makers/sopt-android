/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.appjamtamp.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionListEntity

@Serializable
data class AppjamtampMissionsResponseDto(
    @SerialName("teamNumber")
    val teamNumber: String?,
    @SerialName("teamName")
    val teamName: String?,
    @SerialName("missions")
    val missions: List<AppjamtampMissionItemDto>
) {
    fun toEntity(): AppjamtampMissionListEntity {
        return AppjamtampMissionListEntity(
            teamNumber = teamNumber.orEmpty(),
            teamName = teamName.orEmpty(),
            missions = missions.map { it.toEntity() }
        )
    }
}

@Serializable
data class AppjamtampMissionItemDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("ownerName")
    val ownerName: String? = null,
    @SerialName("level")
    val level: Int,
    @SerialName("profileImage")
    val profileImage: List<String>? = emptyList(),
    @SerialName("isCompleted")
    val isCompleted: Boolean
) {
    fun toEntity(): AppjamtampMissionEntity {
        return AppjamtampMissionEntity(
            id = id,
            title = title,
            ownerName = ownerName,
            level = level,
            profileImage = profileImage,
            isCompleted = isCompleted
        )
    }
}
