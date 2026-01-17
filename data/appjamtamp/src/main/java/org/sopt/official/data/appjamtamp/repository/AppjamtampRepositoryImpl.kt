/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.data.appjamtamp.repository

import dev.zacsweers.metro.Inject
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

@Inject
class AppjamtampRepositoryImpl(
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
