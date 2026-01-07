package org.sopt.official.domain.appjamtamp.repository

import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionListEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionScore
import org.sopt.official.domain.appjamtamp.entity.AppjamtampRecentMission
import org.sopt.official.domain.appjamtamp.entity.AppjamtampStampEntity

interface AppjamtampRepository {
    suspend fun getAppjamtampMissions(
        teamNumber: String? = null,
        isCompleted: Boolean? = null
    ): Result<AppjamtampMissionListEntity>

    suspend fun getAppjamtampStamp(
        missionId: Int,
        nickname: String
    ): Result<AppjamtampStampEntity>

    suspend fun postAppjamtampStamp(
        missionId: Int,
        image: String,
        contents: String,
        activityDate: String
    ): Result<Unit>

    suspend fun getAppjamtampMissionRanking(
        size: Int
    ): Result<List<AppjamtampMissionScore>>

    suspend fun getAppjamtampMissionTop3(
        size: Int
    ): Result<List<AppjamtampRecentMission>>
}
