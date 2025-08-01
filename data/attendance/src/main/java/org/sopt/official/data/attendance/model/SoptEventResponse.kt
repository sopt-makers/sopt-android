/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.data.attendance.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.attendance.entity.AttendanceStatus
import org.sopt.official.domain.attendance.entity.EventType
import org.sopt.official.domain.attendance.entity.SoptEvent

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
                LocalDateTime.parse(attendedAt).run {
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
            val startAtDateTime = LocalDateTime.parse(startAt)
            val endAtDateTime = LocalDateTime.parse(endAt)
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
