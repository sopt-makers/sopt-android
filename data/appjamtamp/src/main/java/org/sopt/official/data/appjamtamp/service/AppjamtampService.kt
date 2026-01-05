package org.sopt.official.data.appjamtamp.service

import org.sopt.official.data.appjamtamp.dto.AppjamtampMissionsResponseDto
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop3RecentMissionResponse
import org.sopt.official.data.appjamtamp.dto.response.AppjamtampTop10MissionScoreResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppjamtampService {
    @GET("appjamtamp/mission")
    suspend fun getAppjamtampMissions(
        @Query("teamNumber") teamNumber: String? = null,
        @Query("isCompleted") isCompleted : Boolean? = null
    ) : AppjamtampMissionsResponseDto

    // 앱잼팀 오늘의 득점 랭킹 TOP10 조회하기
    @GET("appjamrank/today")
    suspend fun getAppjamtampMissionRanking(
        @Query("size") size: Int? = 10
    ): AppjamtampTop10MissionScoreResponse

    // 앱잼팀 랭킹 최근 인증한 미션 TOP3 조회하기
    @GET("appjamrank/recent")
    suspend fun getAppjamtampMissionTop3(
        @Query("size") size: Int? = 3
    ): AppjamtampTop3RecentMissionResponse
}
