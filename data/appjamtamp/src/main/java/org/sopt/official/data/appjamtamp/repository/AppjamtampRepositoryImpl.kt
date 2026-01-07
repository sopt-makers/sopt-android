package org.sopt.official.data.appjamtamp.repository

import javax.inject.Inject
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.appjamtamp.datasource.AppjamtampDataSource
import org.sopt.official.data.appjamtamp.mapper.toDomain
import org.sopt.official.data.appjamtamp.mapper.toEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionListEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionScore
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMyAppjamInfoEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampRecentMission
import org.sopt.official.domain.appjamtamp.entity.AppjamtampStampEntity
import org.sopt.official.domain.appjamtamp.entity.AppjamtampUser
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository

internal class AppjamtampRepositoryImpl @Inject constructor(
    private val appjamtampDataSource: AppjamtampDataSource
) : AppjamtampRepository {
    override suspend fun getAppjamtampMissions(
        teamNumber: String?,
        isCompleted: Boolean?
    ): Result<AppjamtampMissionListEntity> = suspendRunCatching {
        appjamtampDataSource.getAppjamtampMissions(teamNumber, isCompleted).toEntity()
    }

    override suspend fun getAppjamtampStamp(
        missionId: Int,
        nickname: String
    ): Result<AppjamtampStampEntity> = suspendRunCatching {
        appjamtampDataSource.getAppjamtampStamp(missionId, nickname).toEntity()
    }

    override suspend fun postAppjamtampStamp(
        missionId: Int,
        image: String,
        contents: String,
        activityDate: String
    ): Result<AppjamtampUser> = suspendRunCatching {
        appjamtampDataSource.postAppjamtampStamp(
            missionId = missionId,
            image = image,
            contents = contents,
            activityDate = activityDate
        ).toDomain()
    }

    override suspend fun getMyAppjamInfo(): Result<AppjamtampMyAppjamInfoEntity> = suspendRunCatching {
        appjamtampDataSource.getMyAppjamInfo().toEntity()
    }

    override suspend fun getAppjamtampMissionRanking(
        size: Int
    ): Result<List<AppjamtampMissionScore>> = suspendRunCatching {
        appjamtampDataSource.getAppjamtampMissionRanking(size = size).toDomain()
    }

    override suspend fun getAppjamtampMissionTop3(
        size: Int
    ): Result<List<AppjamtampRecentMission>> = suspendRunCatching {
        appjamtampDataSource.getAppjamtampMissionTop3(size = size).toDomain()
    }
}
