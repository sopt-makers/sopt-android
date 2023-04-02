package org.sopt.official.domain.repository.attendance

import org.sopt.official.domain.entity.attendance.SoptEvent

interface AttendanceRepository {
    suspend fun fetchSoptEvent(): Result<SoptEvent>
}