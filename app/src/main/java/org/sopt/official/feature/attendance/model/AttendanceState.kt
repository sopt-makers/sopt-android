package org.sopt.official.feature.attendance.model

sealed class AttendanceState<out T> {
    data object Init : AttendanceState<Nothing>()
    data object Loading : AttendanceState<Nothing>()
    data class Success<T>(val data: T) : AttendanceState<T>()
    data class Failure(val error: Throwable?) : AttendanceState<Nothing>()
}