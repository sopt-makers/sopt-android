package org.sopt.official.data.appjamtamp.repository

import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.appjamtamp.datasource.AppjamtampDataSource
import org.sopt.official.data.appjamtamp.mapper.toDomain
import org.sopt.official.data.appjamtamp.service.AppjamtampService
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionScore
import org.sopt.official.domain.appjamtamp.entity.AppjamtampRecentMission
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMissionEntity
import org.sopt.official.domain.appjamtamp.repository.AppjamtampRepository
import javax.inject.Inject

internal class AppjamtampRepositoryImpl @Inject constructor(
    private val appjamtampDataSource: AppjamtampDataSource
) : AppjamtampRepository {
    override suspend fun getAppjamtampMissions(
        teamNumber: String?,
        isCompleted: Boolean?
    ): Result<List<AppjamtampMissionEntity>> = suspendRunCatching {
        appjamtampDataSource.getAppjamtampMissions(teamNumber, isCompleted).missions.map { it.toEntity() }
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
