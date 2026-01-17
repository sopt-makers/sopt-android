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
package org.sopt.official.feature.appjamtamp.missionlist.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionListEntity
import org.sopt.official.domain.appjamtamp.entity.MissionLevel

data class AppjamtampMissionListUiModel(
    val teamNumber: String = "",
    val teamName: String = "",
    val missionList: ImmutableList<AppjamtampMissionUiModel> = persistentListOf(),
)

fun AppjamtampMissionListEntity.toUiModel() = AppjamtampMissionListUiModel(
    teamNumber = teamNumber,
    teamName = teamName,
    missionList = missions.map { it.toUiModel() }.toImmutableList()
)

data class AppjamtampMissionUiModel(
    val id: Int = -1,
    val title: String = "",
    val ownerName: String? = "",
    val level: MissionLevel = MissionLevel.of(1),
    val profileImage: List<String>? = emptyList(),
    val isCompleted: Boolean = false,
)

fun AppjamtampMissionEntity.toUiModel() = AppjamtampMissionUiModel(
    id = id,
    title = title,
    ownerName = ownerName,
    level = MissionLevel.of(level),
    profileImage = profileImage,
    isCompleted = isCompleted,
)
