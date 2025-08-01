/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.attendance.entity.AttendanceHistory
import org.sopt.official.domain.attendance.entity.AttendanceLog
import org.sopt.official.domain.attendance.entity.AttendanceStatus
import org.sopt.official.domain.attendance.entity.AttendanceSummary
import org.sopt.official.domain.attendance.entity.AttendanceUserInfo
import org.sopt.official.domain.attendance.entity.EventAttribute
import org.sopt.official.domain.attendance.entity.Part

@Serializable
data class AttendanceHistoryResponse(
    @SerialName("part")
    val part: String,
    @SerialName("generation")
    val generation: Int,
    @SerialName("name")
    val name: String,
    @SerialName("score")
    val score: Double,
    @SerialName("total")
    val attendanceCount: AttendanceCountResponse,
    @SerialName("attendances")
    val attendances: List<AttendanceResponse>
) {
    @Serializable
    data class AttendanceCountResponse(
        @SerialName("attendance")
        val normal: Int,
        @SerialName("tardy")
        val late: Int,
        @SerialName("absent")
        val abnormal: Int,
        @SerialName("participate")
        val participate: Int
    ) {
        fun toEntity(): AttendanceSummary = AttendanceSummary(
            normal = this.normal,
            late = this.late,
            abnormal = this.abnormal,
            participate = this.participate
        )
    }

    @Serializable
    data class AttendanceResponse(
        @SerialName("attribute")
        val attribute: String,
        @SerialName("name")
        val eventName: String,
        @SerialName("status")
        val attendanceState: String,
        @SerialName("date")
        val date: String
    ) {
        fun toEntity(): AttendanceLog = AttendanceLog(
            attribute = EventAttribute.valueOf(this.attribute),
            attendanceState = AttendanceStatus.valueOf(this.attendanceState).statusKorean,
            eventName = this.eventName,
            date = this.date
        )
    }

    fun toEntity(): AttendanceHistory = AttendanceHistory(
        userInfo = AttendanceUserInfo(
            generation = this.generation,
            partName = Part.valueOf(this.part).partName,
            userName = this.name,
            attendancePoint = if (this.score % 1 == 0.0) {
                this.score.toInt()
            } else {
                this.score
            }
        ),
        attendanceSummary = this.attendanceCount.toEntity(),
        attendanceLog = this.attendances.map { it.toEntity() }
    )
}
