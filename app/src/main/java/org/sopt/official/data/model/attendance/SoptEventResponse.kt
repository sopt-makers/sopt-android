package org.sopt.official.data.model.attendance

import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.AttendanceStatus
import org.sopt.official.domain.entity.attendance.EventType
import org.sopt.official.domain.entity.attendance.SoptEvent

@Serializable
data class SoptEventResponse(
    val type: String,
    val location: String,
    @SerialName("name")
    val eventName: String,
    val startAt: String,
    val endAt: String,
    val attendances: List<AttendanceResponse>
) {
    @Serializable
    data class AttendanceResponse(
        val status: String,
        val attendedAt: String = ""
    ) {
        fun toEntity(index: Int): SoptEvent.Attendance {
            val attendedAtTime = if (attendedAt != "") {
                attendedAt.toLocalDateTime().run {
                    "${this.hour}:${this.minute}"
                }
            } else {
                "${index + 1}차 출석"
            }
            return SoptEvent.Attendance(
                AttendanceStatus.valueOf(this.status),
                attendedAtTime
            )
        }
    }

    fun toEntity(): SoptEvent {
        val eventDateTime: String = if (startAt != "" && endAt != "") {
            val startAtDateTime = startAt.toLocalDateTime()
            val endAtDateTime = endAt.toLocalDateTime()
            if (startAtDateTime.date == endAtDateTime.date) {
                "${startAtDateTime.monthNumber}월 ${startAtDateTime.dayOfMonth}일 ${
                    startAtDateTime.hour.toString().padStart(2, '0')
                }:${startAtDateTime.minute.toString().padStart(2, '0')} - ${
                    endAtDateTime.hour.toString().padStart(2, '0')
                }:${endAtDateTime.minute.toString().padStart(2, '0')}"
            } else {
                "${startAtDateTime.monthNumber}월 ${startAtDateTime.dayOfMonth}일 ${
                    startAtDateTime.hour.toString().padStart(2, '0')
                }:${
                    startAtDateTime.minute.toString().padStart(2, '0')
                } - ${endAtDateTime.monthNumber}월 ${endAtDateTime.dayOfMonth}일 ${
                    endAtDateTime.hour.toString().padStart(2, '0')
                }:${endAtDateTime.minute.toString().padStart(2, '0')}"
            }
        } else {
            ""
        }

        return SoptEvent(
            eventType = EventType.valueOf(this.type),
            date = eventDateTime,
            location = this.location,
            eventName = this.eventName,
            isAttendancePointAwardedEvent = type == "HAS_ATTENDANCE",
            attendances = this.attendances.mapIndexed { index, attendanceResponse -> attendanceResponse.toEntity(index) }
        )
    }
}
