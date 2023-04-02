package org.sopt.official.domain.entity.attendance

data class SoptEvent(
    val date: String,
    val location: String?,
    val eventName: String,
    val isAttendancePointAwardedEvent: Boolean
)
