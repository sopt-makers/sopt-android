package org.sopt.official.feature.attendance.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.sopt.official.feature.attendance.NewAttendanceViewModel
import org.sopt.official.feature.attendance.model.AttendanceAction
import org.sopt.official.feature.attendance.model.AttendanceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRoute() {

    val viewModel: NewAttendanceViewModel = viewModel()

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val action = AttendanceAction(viewModel)

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "출석") })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (state) {
                AttendanceUiState.Loading -> {
                    AttendanceLoadingScreen()
                }

                is AttendanceUiState.Failure -> {}
                AttendanceUiState.NetworkError -> {}
                is AttendanceUiState.Success -> {
                    AttendanceScreen(state = state as AttendanceUiState.Success, action = action)
                }
            }
        }
    }
}

@Preview
@Composable
private fun AttendanceRoutePreview() {
    AttendanceRoute()
}
