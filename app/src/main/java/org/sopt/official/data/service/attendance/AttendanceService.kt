package org.sopt.official.data.service.attendance

import org.sopt.official.data.model.attendance.SoptEventResponse

interface AttendanceService {
    suspend fun getSoptEvent(): SoptEventResponse
}