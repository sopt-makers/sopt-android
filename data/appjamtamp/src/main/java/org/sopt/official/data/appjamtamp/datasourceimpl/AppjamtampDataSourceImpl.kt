package org.sopt.official.data.appjamtamp.datasourceimpl

import javax.inject.Inject
import org.sopt.official.data.appjamtamp.datasource.AppjamtampDataSource
import org.sopt.official.data.appjamtamp.dto.request.AppjamtampPostStampRequestDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampMissionsResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampMyAppjamInfoResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampPostStampResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampStampResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop10MissionScoreResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop3RecentMissionResponse
import org.sopt.official.data.appjamtamp.service.AppjamtampService

internal class AppjamtampDataSourceImpl @Inject constructor(
    private val appjamtampService: AppjamtampService
) : AppjamtampDataSource {
    override suspend fun getAppjamtampMissions(
        teamNumber: String?,
        isCompleted: Boolean?
    ): AppjamtampMissionsResponseDto =
        appjamtampService.getAppjamtampMissions(teamNumber, isCompleted)

    override suspend fun getAppjamtampStamp(
        missionId: Int,
        nickname: String
    ): AppjamtampStampResponseDto = appjamtampService.getAppjamtampStamp(missionId, nickname)

    override suspend fun postAppjamtampStamp(
        missionId: Int,
        image: String,
        contents: String,
        activityDate: String
    ): AppjamtampPostStampResponseDto = appjamtampService.postAppjamtampStamp(
        AppjamtampPostStampRequestDto(
            missionId = missionId,
            image = image,
            contents = contents,
            activityDate = activityDate
        )
    )

    override suspend fun getMyAppjamInfo(): AppjamtampMyAppjamInfoResponseDto = appjamtampService.getMyAppjamInfo()

    override suspend fun getAppjamtampMissionTop3(size: Int): AppjamtampTop3RecentMissionResponse =
        appjamtampService.getAppjamtampMissionTop3(size = size)

    override suspend fun getAppjamtampMissionRanking(size: Int): AppjamtampTop10MissionScoreResponse =
        appjamtampService.getAppjamtampMissionRanking(size = size)
}
