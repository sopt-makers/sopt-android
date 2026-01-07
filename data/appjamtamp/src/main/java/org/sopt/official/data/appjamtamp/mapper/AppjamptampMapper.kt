package org.sopt.official.data.appjamtamp.mapper

import org.sopt.official.data.appjamtamp.dto.response.AppjamtampMissionScoreResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampRecentMissionResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop10MissionScoreResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop3RecentMissionResponse
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionScore
import org.sopt.official.domain.appjamtamp.entity.AppjamtampRecentMission


internal fun AppjamtampTop3RecentMissionResponse.toDomain(): List<AppjamtampRecentMission> {
    return this.ranks.map { it.toDomain() }
}

internal fun AppjamtampRecentMissionResponse.toDomain(): AppjamtampRecentMission {
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

internal fun AppjamtampTop10MissionScoreResponse.toDomain(): List<AppjamtampMissionScore> {
    return this.ranks.map { it.toDomain() }
}

internal fun AppjamtampMissionScoreResponse.toDomain(): AppjamtampMissionScore {
    return AppjamtampMissionScore(
        rank = this.rank,
        teamName = this.teamName,
        teamNumber = this.teamNumber,
        todayPoints = this.todayPoints,
        totalPoints = this.todayPoints
    )
}

