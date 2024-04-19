package org.sopt.official.feature.attendance.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.sopt.official.feature.attendance.model.AttendanceAction
import org.sopt.official.feature.attendance.model.AttendanceUiState

@Composable
fun AttendanceScreen(state: AttendanceUiState.Success, action: AttendanceAction) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

    }
}
