package org.sopt.official.data.model.attendance

import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.SoptEvent

@Serializable
data class SoptEventResponse(
    val date: String,
    val location: String?,
    val eventName: String,
    val isAttendancePointAwardedEvent: Boolean
) {
    fun toEntity() = SoptEvent(date, location, eventName, isAttendancePointAwardedEvent)
}
