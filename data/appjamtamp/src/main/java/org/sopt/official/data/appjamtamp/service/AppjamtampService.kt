package org.sopt.official.data.appjamtamp.service

import org.sopt.official.data.appjamtamp.dto.AppjamtampMissionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppjamtampService {
    @GET("appjamtamp/mission")
    suspend fun getAppjamtampMissions(
        @Query("teamNumber") teamNumber: String,
        @Query("isCompleted") isCompleted : Boolean? = null
    ) : AppjamtampMissionsResponse
}