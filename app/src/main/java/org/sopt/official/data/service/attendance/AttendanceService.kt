package org.sopt.official.data.service.attendance

import org.sopt.official.data.model.attendance.AttendanceHistoryResponse
import org.sopt.official.data.model.attendance.BaseAttendanceResponse
import org.sopt.official.data.model.attendance.SoptEventResponse
import retrofit2.http.GET

interface AttendanceService {
    @GET("/api/v1/app/lecture")
    suspend fun getSoptEvent(): BaseAttendanceResponse<SoptEventResponse>

    @GET("/api/v1/app/total")
    suspend fun getAttendanceHistory(): BaseAttendanceResponse<AttendanceHistoryResponse>
}