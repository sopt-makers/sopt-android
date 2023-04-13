package org.sopt.official.domain.entity.attendance

data class SoptEvent(
    val id: Int,
    val eventType: EventType,
    val date: String,
    val location: String,
    val eventName: String,
    val message: String,
    val isAttendancePointAwardedEvent: Boolean,
    val attendances: List<Attendance>
) {
    data class Attendance(
        val status: AttendanceStatus,
        val attendedAt: String
    )
}
