package org.sopt.official.domain.repository.attendance

import org.sopt.official.domain.entity.attendance.AttendanceHistory
import org.sopt.official.domain.entity.attendance.AttendanceRound
import org.sopt.official.domain.entity.attendance.SoptEvent

interface AttendanceRepository {
    suspend fun fetchSoptEvent(): Result<SoptEvent>
    suspend fun fetchAttendanceHistory(): Result<AttendanceHistory>
    suspend fun fetchAttendanceRound(lectureId: Long): Result<AttendanceRound>
}