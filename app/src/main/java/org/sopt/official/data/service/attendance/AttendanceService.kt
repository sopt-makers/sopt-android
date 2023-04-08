package org.sopt.official.data.service.attendance

import org.sopt.official.data.model.attendance.AttendanceHistoryResponse
import org.sopt.official.data.model.attendance.BaseAttendanceResponse
import org.sopt.official.data.model.attendance.SoptEventResponse

interface AttendanceService {
    suspend fun getSoptEvent(): BaseAttendanceResponse<SoptEventResponse>
    suspend fun getAttendanceHistory(): BaseAttendanceResponse<AttendanceHistoryResponse>
}