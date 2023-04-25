package org.sopt.official.data.service.attendance

import org.sopt.official.data.model.attendance.*
import retrofit2.http.GET
import retrofit2.http.Path

interface AttendanceService {
    @GET("/api/v1/app/lecture")
    suspend fun getSoptEvent(): BaseAttendanceResponse<SoptEventResponse>

    @GET("/api/v1/app/total")
    suspend fun getAttendanceHistory(): BaseAttendanceResponse<AttendanceHistoryResponse>

    @GET("/app/lectures/round/{lectureId}")
    suspend fun getAttendanceRound(
        @Path("lectureId") lectureId: Long
    ): BaseAttendanceResponse<AttendanceRoundResponse>

    @GET("/app/attendances/attend")
    suspend fun confirmAttendanceCode(
        param: RequestAttendanceCode
    ): BaseAttendanceResponse<AttendanceCodeResponse>
}