package org.sopt.official.data.appjamtamp.datasource

import org.sopt.official.data.appjamtamp.service.AppjamtampService
import javax.inject.Inject

class AppjamtampDataSource @Inject constructor(
    private val appjamtampService: AppjamtampService
) {
    suspend fun getAppjamtampMissions(
        teamNumber: String,
        isCompleted: Boolean? = null
    ) = appjamtampService.getAppjamtampMissions(teamNumber, isCompleted)
}