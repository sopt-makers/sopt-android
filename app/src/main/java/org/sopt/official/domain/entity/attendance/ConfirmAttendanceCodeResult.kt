package org.sopt.official.domain.entity.attendance

sealed interface ConfirmAttendanceCodeResult {
    data object Success : ConfirmAttendanceCodeResult
    data class Failure(val errorMessage: String?) : ConfirmAttendanceCodeResult
}