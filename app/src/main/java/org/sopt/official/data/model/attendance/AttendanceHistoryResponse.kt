package org.sopt.official.data.model.attendance

import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.attendance.AttendanceHistory
import org.sopt.official.domain.entity.attendance.AttendanceLog
import org.sopt.official.domain.entity.attendance.AttendanceSummary
import org.sopt.official.domain.entity.attendance.AttendanceUserInfo

@Serializable
data class AttendanceHistoryResponse(
    val userInfo: AttendanceUserInfoResponse,
    val attendanceSummary: AttendanceSummaryResponse,
    val attendanceLog: List<AttendanceLogResponse>
) {
    @Serializable
    data class AttendanceUserInfoResponse(
        val generation: Int,
        val partName: String,
        val userName: String,
        val attendancePoint: Double
    ) {
        fun toEntity() = AttendanceUserInfo(
            this.generation,
            this.partName,
            this.userName,
            this.attendancePoint
        )
    }

    @Serializable
    data class AttendanceSummaryResponse(
        val all: Int,
        val normal: Int,
        val late: Int,
        val abnormal: Int
    ) {
        fun toEntity() = AttendanceSummary(
            this.all,
            this.normal,
            this.late,
            this.abnormal
        )
    }

    @Serializable
    data class AttendanceLogResponse(
        val attendanceState: String,
        val eventName: String,
        val date: String
    ) {
        fun toEntity() = AttendanceLog(
            this.attendanceState,
            this.eventName,
            this.date
        )
    }

    fun toEntity() =
        AttendanceHistory(this.userInfo.toEntity(), this.attendanceSummary.toEntity(), this.attendanceLog.map { it.toEntity() })
}
