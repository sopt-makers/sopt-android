package org.sopt.official.feature.attendance.model

sealed interface AttendanceUiState {
    data object Loading : AttendanceUiState
    data class Success(
        val fakeTitle: String
    ) : AttendanceUiState

    data class Failure(val error: Throwable?) : AttendanceUiState
    data object NetworkError : AttendanceUiState
}
