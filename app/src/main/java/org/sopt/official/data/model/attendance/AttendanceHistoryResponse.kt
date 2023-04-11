package org.sopt.official.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.*

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
