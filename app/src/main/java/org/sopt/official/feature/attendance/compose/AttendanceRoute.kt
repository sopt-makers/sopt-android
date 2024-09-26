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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.sopt.official.feature.attendance.NewAttendanceViewModel
import org.sopt.official.feature.attendance.compose.component.AttendanceTopAppBar
import org.sopt.official.feature.attendance.model.AttendanceAction
import org.sopt.official.feature.attendance.model.AttendanceUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRoute(onClickBackIcon: () -> Unit) {
    val viewModel: NewAttendanceViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val action = viewModel.rememberAttendanceActions()

    Scaffold(
        topBar = {
            AttendanceTopAppBar(
                onClickBackIcon = onClickBackIcon,
                onClickRefreshIcon = viewModel::fetchData,
            )
        }
    ) { innerPaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingValues)
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

@Composable
fun NewAttendanceViewModel.rememberAttendanceActions(): AttendanceAction = remember(this) {
    AttendanceAction(
        onFakeClick = this::updateUiState
    )
}
