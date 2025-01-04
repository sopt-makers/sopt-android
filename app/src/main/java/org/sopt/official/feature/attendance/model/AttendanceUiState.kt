package org.sopt.official.feature.attendance.model

import androidx.annotation.DrawableRes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import org.sopt.official.R

sealed interface AttendanceUiState {
    data object Loading : AttendanceUiState
    data class Success(
        val attendanceDayType: AttendanceDayType,
        val userTitle: String,
        val attendanceScore: Float,
        val totalAttendanceResult: ImmutableMap<AttendanceResultType, Int>,
        val attendanceHistoryList: ImmutableList<AttendanceHistory>,
    ) : AttendanceUiState {
        sealed interface AttendanceDayType {
            /** 출석이 진행되는 날 **/
            data class AttendanceDay(
                val eventDate: String,
                val eventLocation: String,
                val eventName: String,
                val firstAttendance: MidtermAttendance,
                val secondAttendance: MidtermAttendance,
                val finalAttendance: FinalAttendance,
            ) : AttendanceDayType {
                sealed class MidtermAttendance private constructor(
                    @DrawableRes val imageResId: Int,
                    val isFinished: Boolean,
                    val description: String,
                ) {
                    data class NotYet(val attendanceSession: AttendanceSession) : MidtermAttendance(
                        imageResId = R.drawable.ic_attendance_state_nothing,
                        isFinished = false,
                        description = attendanceSession.type
                    ) {
                        enum class AttendanceSession(val type: String) {
                            FIRST("1차 출석"),
                            SECOND("2차 출석")
                        }
                    }

                    data class Present(val attendanceAt: String) : MidtermAttendance(
                        imageResId = R.drawable.ic_attendance_state_yes,
                        isFinished = true,
                        description = attendanceAt
                    )

                    data object Absent : MidtermAttendance(
                        imageResId = R.drawable.ic_attendance_state_absence_white,
                        isFinished = true,
                        description = "-"
                    )
                }

                enum class FinalAttendance(
                    @DrawableRes val imageResId: Int,
                    val isFinished: Boolean,
                    val result: String,
                ) {
                    NOT_YET(
                        imageResId = R.drawable.ic_attendance_state_nothing,
                        isFinished = false,
                        result = "출석 전"
                    ),
                    PRESENT(
                        imageResId = R.drawable.ic_attendance_state_done,
                        isFinished = true,
                        result = "출석완료!"
                    ),
                    LATE(
                        imageResId = R.drawable.ic_attendance_state_late,
                        isFinished = true,
                        result = "지각"
                    ),
                    ABSENT(
                        imageResId = R.drawable.ic_attendance_state_absence_black,
                        isFinished = true,
                        result = "결석"
                    )
                }
            }

            /** 출석할 필요가 없는 날 **/
            data class Event(
                val eventDate: String,
                val eventLocation: String,
                val eventName: String,
            ) : AttendanceDayType

            /** 아무 일정이 없는 날 **/
            data object None : AttendanceDayType
        }

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
    }

    data class Failure(val error: Throwable?) : AttendanceUiState
    data object NetworkError : AttendanceUiState
}
