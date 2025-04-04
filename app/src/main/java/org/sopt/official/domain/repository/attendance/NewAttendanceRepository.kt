package org.sopt.official.domain.repository.attendance

import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.domain.entity.attendance.ConfirmAttendanceCodeResult
import org.sopt.official.domain.entity.attendance.FetchAttendanceCurrentRoundResult

interface NewAttendanceRepository {
    suspend fun fetchAttendanceInfo(): Attendance
    suspend fun fetchAttendanceCurrentRound(lectureId: Long): FetchAttendanceCurrentRoundResult
    suspend fun confirmAttendanceCode(subLectureId: Long, code: String): ConfirmAttendanceCodeResult
}
