package org.sopt.official.data.model.attendance

import org.sopt.official.domain.entity.attendance.SoptEvent

@kotlinx.serialization.Serializable
data class SoptEventResponse(
    val date: String,
    val location: String?,
    val eventName: String,
    val isAttendancePointAwardedEvent: Boolean
) {
    fun toEntity() = SoptEvent(date, location, eventName, isAttendancePointAwardedEvent)
}
