package org.sopt.official.data.appjamtamp.datasource

import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop10MissionScoreResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop3RecentMissionResponse
import org.sopt.official.data.appjamtamp.dto.AppjamtampMissionsResponseDto

interface AppjamtampDataSource {
    suspend fun getAppjamtampMissions(
        teamNumber: String? = null,
        isCompleted: Boolean? = null
    ): AppjamtampMissionsResponseDto
    suspend fun getAppjamtampMissionTop3(size: Int): AppjamtampTop3RecentMissionResponse
    suspend fun getAppjamtampMissionRanking(size: Int): AppjamtampTop10MissionScoreResponse
}