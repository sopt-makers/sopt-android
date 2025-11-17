/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.navigation

import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.MissionLevel

sealed interface SoptampRoute

@Serializable
data object MissionList : SoptampRoute

@Serializable
data class MissionDetail(
    val id: Int,
    val title: String,
    val level: Int,
    val isCompleted: Boolean,
    val isMe: Boolean,
    val nickname: String,
    val myName: String,
) : SoptampRoute

@Serializable
data class Ranking(val type: String, val entrySource: String) : SoptampRoute

@Serializable
data object PartRanking : SoptampRoute

@Serializable
data class UserMissionList(
    val nickname: String,
    val description: String?,
    val entrySource: String?
) : SoptampRoute

@Serializable
data object Onboarding : SoptampRoute

// Extension functions for converting from existing models
fun org.sopt.official.stamp.feature.mission.model.MissionNavArgs.toRoute() =
    MissionDetail(
        id = this.id,
        title = this.title,
        level = this.level.value,
        isCompleted = this.isCompleted,
        isMe = this.isMe,
        nickname = this.nickname,
        myName = this.myName,
    )

fun MissionDetail.toMissionLevel(): MissionLevel = MissionLevel.of(this.level)

fun org.sopt.official.stamp.feature.ranking.model.RankerNavArg.toRoute() =
    UserMissionList(
        nickname = this.nickname,
        description = this.description,
        entrySource = this.entrySource,
    )
