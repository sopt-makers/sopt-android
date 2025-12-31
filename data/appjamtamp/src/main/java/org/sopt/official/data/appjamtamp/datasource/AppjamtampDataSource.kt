package org.sopt.official.data.appjamtamp.datasource

import org.sopt.official.data.appjamtamp.dto.AppjamtampMissionsResponseDto

interface AppjamtampDataSource {
    suspend fun getAppjamtampMissions(
        teamNumber: String,
        isCompleted: Boolean? = null
    ): AppjamtampMissionsResponseDto
}