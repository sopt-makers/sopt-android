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
package org.sopt.official.data.appjamtamp.service

import org.sopt.official.data.appjamtamp.dto.request.AppjamtampPostStampRequestDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampMissionsResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampMyAppjamInfoResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampPostStampResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampStampResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop10MissionScoreResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop3RecentMissionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppjamtampService {
    @GET("appjamtamp/mission")
    suspend fun getAppjamtampMissions(
        @Query("teamNumber") teamNumber: String? = null,
        @Query("isCompleted") isCompleted: Boolean? = null
    ): AppjamtampMissionsResponseDto

    @GET("appjamtamp/stamp")
    suspend fun getAppjamtampStamp(
        @Query("missionId") missionId: Int,
        @Query("nickname") nickname: String
    ): AppjamtampStampResponseDto

    @POST("appjamtamp/stamp")
    suspend fun postAppjamtampStamp(
        @Body body: AppjamtampPostStampRequestDto
    ): AppjamtampPostStampResponseDto

    @GET("user/appjam-info")
    suspend fun getMyAppjamInfo(): AppjamtampMyAppjamInfoResponseDto

    /**
     * 앱잼에 참여하는 전체 팀의 득점 랭킹 조회
     * - 서버 명세에서 API명: 앱잼팀 득점 랭킹 TOP10 조회
     * - 실제로는 모든 팀의 순위를 조회 해야함. 전체 팀 수(예: 12팀)를 [size]에 전달해야 함
     * * @param size 조회할 팀의 수 (기본값 10)
     */
    @GET("appjamrank/today")
    suspend fun getAppjamtampMissionRanking(
        @Query("size") size: Int? = 10
    ): AppjamtampTop10MissionScoreResponse

    @GET("appjamrank/recent")
    suspend fun getAppjamtampMissionTop3(
        @Query("size") size: Int? = 3
    ): AppjamtampTop3RecentMissionResponse
}
