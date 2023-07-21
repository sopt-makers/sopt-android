package org.sopt.official.data.model.attendance

import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.AttendanceStatus
import org.sopt.official.domain.entity.attendance.EventType
import org.sopt.official.domain.entity.attendance.SoptEvent

@Serializable
data class SoptEventResponse(
    @SerialName("id")
    val id: Int = 1,
    @SerialName("type")
    val type: String,
    @SerialName("location")
    val location: String,
    @SerialName("name")
    val eventName: String,
    @SerialName("startDate")
    val startAt: String,
    @SerialName("endDate")
    val endAt: String,
    @SerialName("message")
    val message: String = "",
    @SerialName("attendances")
    val attendances: List<AttendanceResponse>
) {
    @Serializable
    data class AttendanceResponse(
        @SerialName("status")
        val status: String,
        @SerialName("attendedAt")
        val attendedAt: String = ""
    ) {
        fun toEntity(index: Int): SoptEvent.Attendance {
            val attendedAtTime = if (this.status == "ATTENDANCE") {
                attendedAt.toLocalDateTime().run {
                    "${this.hour.toString().padStart(2, '0')}:${this.minute.toString().padStart(2, '0')}"
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
            id = this.id,
            eventType = EventType.valueOf(this.type),
            date = eventDateTime,
            location = this.location,
            eventName = this.eventName,
            message = this.message,
            isAttendancePointAwardedEvent = type == "HAS_ATTENDANCE",
            attendances = this.attendances.mapIndexed { index, attendanceResponse -> attendanceResponse.toEntity(index) }
        )
    }
}
