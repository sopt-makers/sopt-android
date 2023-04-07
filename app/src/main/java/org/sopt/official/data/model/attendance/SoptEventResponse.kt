package org.sopt.official.data.model.attendance

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.SoptEvent

@Serializable
data class SoptEventResponse(
    val type: String,
    val location: String,
    @SerialName("name")
    val eventName: String,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?,
    val attendances: List<AttendanceResponse>
) {
    @Serializable
    data class AttendanceResponse(
        val status: String,
        val attendedAt: LocalDateTime? = null
    ) {
        fun toEntity(): SoptEvent.Attendance = SoptEvent.Attendance(status, attendedAt)
    }

    fun toEntity(): SoptEvent {
        val eventDateTime: String? = if (startAt != null && endAt != null) {
            if (startAt.date == endAt.date) {
                "${startAt.monthNumber}월 ${startAt.dayOfMonth}일 ${
                    startAt.hour.toString().padStart(2, '0')
                }:${startAt.minute.toString().padStart(2, '0')} - ${
                    endAt.hour.toString().padStart(2, '0')
                }:${endAt.minute.toString().padStart(2, '0')}"
            } else {
                "${startAt.monthNumber}월 ${startAt.dayOfMonth}일 ${
                    startAt.hour.toString().padStart(2, '0')
                }:${
                    startAt.minute.toString().padStart(2, '0')
                } - ${endAt.monthNumber}월 ${endAt.dayOfMonth}일 ${
                    endAt.hour.toString().padStart(2, '0')
                }:${endAt.minute.toString().padStart(2, '0')}"
            }
        } else {
            null
        }

        return SoptEvent(
            eventType = this.type,
            date = eventDateTime,
            location = location,
            eventName = this.eventName,
            isAttendancePointAwardedEvent = type == "HAS_ATTENDANCE",
            attendances = this.attendances.map { it.toEntity() }
        )
    }
}
