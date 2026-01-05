package org.sopt.official.domain.appjamtamp.repository

import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionScore
import org.sopt.official.domain.appjamtamp.entity.AppjamtampRecentMission

interface AppjamtampRepository {
    suspend fun getAppjamtampMissions(
        teamNumber: String? = null,
        isCompleted: Boolean? = null
    ): Result<List<AppjamtampMissionEntity>>
    suspend fun getAppjamtampMissionRanking(
        size: Int
    ): Result<List<AppjamtampMissionScore>>
    suspend fun getAppjamtampMissionTop3(
        size: Int
    ): Result<List<AppjamtampRecentMission>>
}
