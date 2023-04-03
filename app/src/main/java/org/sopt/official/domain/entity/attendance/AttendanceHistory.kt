package org.sopt.official.domain.entity.attendance

data class AttendanceHistory(
    val userInfo: AttendanceUserInfo,
    val attendanceSummary: AttendanceSummary,
    val attendanceLog: List<AttendanceLog>
)
