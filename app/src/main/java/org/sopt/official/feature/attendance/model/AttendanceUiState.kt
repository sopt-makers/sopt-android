package org.sopt.official.feature.attendance.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList
import org.sopt.official.domain.entity.attendance.Attendance
import org.sopt.official.feature.attendance.toTotalAttendanceResult
import org.sopt.official.feature.attendance.toUiAttendanceDayType

sealed interface AttendanceUiState {
    data object Loading : AttendanceUiState
    data class Success(
        val sessionId: Long,
        val attendanceDayType: AttendanceDayType,
        val userTitle: String,
        val attendanceScore: Float,
        val totalAttendanceResult: ImmutableMap<AttendanceResultType, Int>,
        val attendanceHistoryList: ImmutableList<AttendanceHistory>,
    ) : AttendanceUiState {

        enum class AttendanceResultType(val type: String) {
            ALL(type = "전체"),
            PRESENT(type = "출석"),
            LATE(type = "지각"),
            ABSENT(type = "결석");
        }

        data class AttendanceHistory(
            val status: String,
            val eventName: String,
            val date: String,
        )

        companion object {
            fun of(attendance: Attendance): Success {
                return Success(
                    sessionId = attendance.lectureId,
                    attendanceDayType = attendance.attendanceDayType.toUiAttendanceDayType(),
                    userTitle = "${attendance.user.generation}기 ${attendance.user.part.partName}파트 ${attendance.user.name}",
                    attendanceScore = attendance.user.attendanceScore.toFloat(),
                    totalAttendanceResult = attendance.user.attendanceCount.toTotalAttendanceResult(),
                    attendanceHistoryList = attendance.user.attendanceHistory.map { attendanceLog: Attendance.User.AttendanceLog ->
                        AttendanceHistory(
                            status = when (attendanceLog.attendanceState) {
                                Attendance.User.AttendanceLog.AttendanceState.PARTICIPATE -> "참여"
                                Attendance.User.AttendanceLog.AttendanceState.ATTENDANCE -> "출석"
                                Attendance.User.AttendanceLog.AttendanceState.TARDY -> "지각"
                                Attendance.User.AttendanceLog.AttendanceState.ABSENT -> "결석"
                            },
                            eventName = attendanceLog.sessionName,
                            date = attendanceLog.date
                        )
                    }.toPersistentList()
                )
            }
        }
    }

    data class Failure(val error: Throwable?) : AttendanceUiState
    data object NetworkError : AttendanceUiState
}
