/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.model.attendance

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.data.model.attendance.TimeFormat.timeFormat
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
                LocalDateTime.parse(attendedAt).run {
                    val localDateTime = LocalDateTime.parse(attendedAt)
                    timeFormat.format(localDateTime)
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
        val eventDateTime: String = if (startAt.isNotBlank() && endAt.isNotBlank()) {
            val startAtDateTime = LocalDateTime.parse(startAt)
            val endAtDateTime = LocalDateTime.parse(endAt)

            if (startAtDateTime.date == endAtDateTime.date) {
                "${startAtDateTime.monthNumber}월 ${startAtDateTime.dayOfMonth}일 " +
                    "${timeFormat.format(startAtDateTime)} - " +
                    timeFormat.format(endAtDateTime)
            } else {
                "${startAtDateTime.monthNumber}월 ${startAtDateTime.dayOfMonth}일 " +
                    "${timeFormat.format(startAtDateTime)} - " +
                    "${endAtDateTime.monthNumber}월 ${endAtDateTime.dayOfMonth}일 " +
                    timeFormat.format(endAtDateTime)
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

object TimeFormat{
    private const val FORMAT_PATTERN = "HH:mm"

    @OptIn(FormatStringsInDatetimeFormats::class)
    val timeFormat = LocalDateTime.Format {
        byUnicodePattern(FORMAT_PATTERN)
    }
}
