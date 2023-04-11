package org.sopt.official.domain.entity.attendance

data class AttendanceLog(
    val attribute: EventAttribute,
    val attendanceState: String,
    val eventName: String,
    val date: String
)
