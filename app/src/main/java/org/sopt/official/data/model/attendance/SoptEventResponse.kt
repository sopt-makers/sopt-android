package org.sopt.official.data.model.attendance

import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.SoptEvent

@Serializable
data class SoptEventResponse(
    val isEventDay: Boolean,
    val date: String? = null,
    val location: String? = null,
    val eventName: String? = null,
    val isAttendancePointAwardedEvent: Boolean? = null
) {
    fun toEntity() = SoptEvent(isEventDay, date, location, eventName, isAttendancePointAwardedEvent)
}
