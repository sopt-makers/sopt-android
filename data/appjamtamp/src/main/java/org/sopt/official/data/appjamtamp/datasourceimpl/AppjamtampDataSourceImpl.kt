package org.sopt.official.data.appjamtamp.datasourceimpl

import org.sopt.official.data.appjamtamp.datasource.AppjamtampDataSource
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop10MissionScoreResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop3RecentMissionResponse
import org.sopt.official.data.appjamtamp.service.AppjamtampService
import javax.inject.Inject

internal class AppjamtampDataSourceImpl @Inject constructor(
    private val appjamtampService: AppjamtampService
) : AppjamtampDataSource {

    override suspend fun getAppjamtampMissionTop3(size: Int): AppjamtampTop3RecentMissionResponse {
        return appjamtampService.getAppjamtampMissionTop3(size = size)
    }

    override suspend fun getAppjamtampMissionRanking(size: Int): AppjamtampTop10MissionScoreResponse {
        return appjamtampService.getAppjamtampMissionRanking(size = size)
    }
}
