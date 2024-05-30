package org.sopt.official.feature.attendance.model.state

import org.sopt.official.feature.attendance.model.AttendanceResultType

class AttendanceHistoryCardState(
    val userTitle: String,
    val attendanceScore: Int,
    val totalAttendanceResult: Map<AttendanceResultType, Int>
)
