package org.sopt.official.data.appjamtamp.service

import org.sopt.official.data.appjamtamp.dto.AppjamtampMissionsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AppjamtampService {
    @GET("appjamtamp/mission")
    suspend fun getAppjamtampMissions(
        @Query("teamNumber") teamNumber: String,
        @Query("isCompleted") isCompleted : Boolean? = null
    ) : AppjamtampMissionsResponseDto
}