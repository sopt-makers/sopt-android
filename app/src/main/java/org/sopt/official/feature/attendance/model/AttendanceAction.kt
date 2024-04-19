package org.sopt.official.feature.attendance.model

import org.sopt.official.feature.attendance.NewAttendanceViewModel

class AttendanceAction(private val viewModel: NewAttendanceViewModel) {
    val onFakeClick: () -> Unit = viewModel::updateUiState
}