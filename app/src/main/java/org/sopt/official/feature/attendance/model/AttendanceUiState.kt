package org.sopt.official.feature.attendance.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

sealed interface AttendanceUiState {
    data object Loading : AttendanceUiState
    data class Success(
        val attendanceDayType: AttendanceDayType,
        val userTitle: String,
        val attendanceScore: Int,
        val totalAttendanceResult: ImmutableMap<AttendanceResultType, Int>,
        val attendanceHistoryList: ImmutableList<AttendanceHistory>,
    ) : AttendanceUiState

    data class Failure(val error: Throwable?) : AttendanceUiState
    data object NetworkError : AttendanceUiState
}
