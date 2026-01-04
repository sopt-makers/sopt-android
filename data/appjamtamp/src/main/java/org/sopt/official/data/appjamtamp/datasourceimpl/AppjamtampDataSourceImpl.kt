package org.sopt.official.data.appjamtamp.datasourceimpl

import org.sopt.official.data.appjamtamp.datasource.AppjamtampDataSource
import org.sopt.official.data.appjamtamp.dto.AppjamtampMissionsResponseDto
import org.sopt.official.data.appjamtamp.service.AppjamtampService
import javax.inject.Inject

internal class AppjamtampDataSourceImpl @Inject constructor(
    private val appjamtampService: AppjamtampService
) : AppjamtampDataSource {
    override suspend fun getAppjamtampMissions(
        teamNumber: String?,
        isCompleted: Boolean?
    ): AppjamtampMissionsResponseDto =
        appjamtampService.getAppjamtampMissions(teamNumber, isCompleted)
}