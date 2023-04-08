package org.sopt.official.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.*

@Serializable
data class AttendanceHistoryResponse(
    val part: String,
    val generation: Int,
    val name: String,
    val score: Double,
    @SerialName("total")
    val attendanceCount: AttendanceCountResponse,
    val attendances: List<AttendanceResponse>
) {
    @Serializable
    data class AttendanceCountResponse(
        @SerialName("total")
        val all: Int,
        @SerialName("attendance")
        val normal: Int,
        @SerialName("tardy")
        val late: Int,
        @SerialName("absent")
        val abnormal: Int
    ) {
        fun toEntity(): AttendanceSummary = AttendanceSummary(
            all = this.all,
            normal = this.normal,
            late = this.late,
            abnormal = this.abnormal
        )
    }

    @Serializable
    data class AttendanceResponse(
        @SerialName("name")
        val eventName: String,
        @SerialName("status")
        val attendanceState: String,
        val date: String
    ) {
        fun toEntity(): AttendanceLog = AttendanceLog(
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
