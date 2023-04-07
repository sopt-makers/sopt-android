package org.sopt.official.domain.entity.attendance

import kotlinx.datetime.LocalDateTime

data class SoptEvent(
    val eventType: String,
    val date: String?,
    val location: String?,
    val eventName: String?,
    val isAttendancePointAwardedEvent: Boolean?,
    val attendances: List<Attendance>
) {
    data class Attendance(
        val status: String,
        val attendedAt: LocalDateTime?
    )
}
