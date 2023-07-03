package org.sopt.official.data.service.attendance

import org.sopt.official.data.model.attendance.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AttendanceService {
    @GET("/api/v1/app/lectures")
    suspend fun getSoptEvent(): BaseAttendanceResponse<SoptEventResponse>

    @GET("/api/v1/app/members/attendances")
    suspend fun getAttendanceHistory(): BaseAttendanceResponse<AttendanceHistoryResponse>

    @GET("/api/v1/app/lectures/round/{lectureId}")
    suspend fun getAttendanceRound(
        @Path("lectureId") lectureId: Long
    ): BaseAttendanceResponse<AttendanceRoundResponse>

    @POST("/api/v1/app/attendances/attend")
    suspend fun confirmAttendanceCode(
        @Body param: RequestAttendanceCode
    ): BaseAttendanceResponse<AttendanceCodeResponse>
}