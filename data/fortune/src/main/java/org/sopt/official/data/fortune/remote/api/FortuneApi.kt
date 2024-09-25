package org.sopt.official.data.fortune.remote.api

import org.sopt.official.data.fortune.remote.response.TodayFortuneCardResponse
import org.sopt.official.data.fortune.remote.response.TodayFortuneWordResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface FortuneApi {

    @GET("api/v2/fortune/word")
    suspend fun getTodayFortuneWord(
        @Query("todayDate") todayDate: String,
    ): TodayFortuneWordResponse

    @GET("api/v2/fortune/card/today")
    suspend fun getTodayFortuneCard(): TodayFortuneCardResponse
}
