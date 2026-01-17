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
package org.sopt.official.data.appjamtamp.mapper

import org.sopt.official.data.appjamtamp.dto.response.AppjamtampMissionScoreResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampRecentMissionResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop10MissionScoreResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop3RecentMissionResponse
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionScore
import org.sopt.official.domain.appjamtamp.entity.AppjamtampRecentMission


fun AppjamtampTop3RecentMissionResponse.toDomain(): List<AppjamtampRecentMission> {
    return this.ranks.map { it.toDomain() }
}

fun AppjamtampRecentMissionResponse.toDomain(): AppjamtampRecentMission {
    return AppjamtampRecentMission(
        stampId = this.stampId,
        missionId = this.missionId,
        userId = this.userId,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt,
        userName = this.userName,
        userProfileImage = this.userProfileImage,
        teamName = this.teamName,
        teamNumber = this.teamNumber
    )
}

fun AppjamtampTop10MissionScoreResponse.toDomain(): List<AppjamtampMissionScore> {
    return this.ranks.map { it.toDomain() }
}

fun AppjamtampMissionScoreResponse.toDomain(): AppjamtampMissionScore {
    return AppjamtampMissionScore(
        rank = this.rank,
        teamName = this.teamName,
        teamNumber = this.teamNumber,
        todayPoints = this.todayPoints,
        totalPoints = this.totalPoints
    )
}
