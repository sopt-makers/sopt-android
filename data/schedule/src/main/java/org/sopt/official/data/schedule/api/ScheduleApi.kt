package org.sopt.official.data.schedule.api

import org.sopt.official.data.schedule.dto.ScheduleResponse
import retrofit2.http.GET

interface ScheduleApi {
    @GET("calendar/all")
    suspend fun getSchedule(): List<ScheduleResponse>
}
