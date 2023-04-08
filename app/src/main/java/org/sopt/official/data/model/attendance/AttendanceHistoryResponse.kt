package org.sopt.official.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.AttendanceHistory
import org.sopt.official.domain.entity.attendance.AttendanceLog
import org.sopt.official.domain.entity.attendance.AttendanceSummary
import org.sopt.official.domain.entity.attendance.AttendanceUserInfo

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
            attendanceState = this.attendanceState,
            eventName = this.eventName,
            date = this.date
        )
    }

    fun toEntity(): AttendanceHistory = AttendanceHistory(
        userInfo = AttendanceUserInfo(
            generation = this.generation,
            partName = this.part,
            userName = this.name,
            attendancePoint = this.score
        ),
        attendanceSummary = this.attendanceCount.toEntity(),
        attendanceLog = this.attendances.map { it.toEntity() }
    )
}
