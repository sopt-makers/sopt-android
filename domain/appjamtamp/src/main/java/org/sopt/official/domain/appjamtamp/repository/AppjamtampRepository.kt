package org.sopt.official.domain.appjamtamp.repository

import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionEntity

interface AppjamtampRepository {
    suspend fun getAppjamtampMissions(
        teamNumber: String,
        isCompleted: Boolean? = null
    ): Result<List<AppjamtampMissionEntity>>
}