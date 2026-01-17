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
package org.sopt.official.data.appjamtamp.datasourceimpl

import dev.zacsweers.metro.Inject
import org.sopt.official.data.appjamtamp.datasource.AppjamtampDataSource
import org.sopt.official.data.appjamtamp.dto.request.AppjamtampPostStampRequestDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampMissionsResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampMyAppjamInfoResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampPostStampResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampStampResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop10MissionScoreResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop3RecentMissionResponse
import org.sopt.official.data.appjamtamp.service.AppjamtampService

@Inject
class AppjamtampDataSourceImpl(
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
