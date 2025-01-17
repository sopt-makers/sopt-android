package org.sopt.official.feature.attendance.compose

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.sopt.official.common.view.toast
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.NewAttendanceViewModel
import org.sopt.official.feature.attendance.compose.component.AttendanceTopAppBar
import org.sopt.official.feature.attendance.model.AttendanceUiState

@Composable
fun AttendanceRoute() {
    val viewModel: NewAttendanceViewModel = viewModel()
    val state: AttendanceUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity: Activity = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect { message ->
            activity.toast(message)
        }
    }

    Scaffold(
        topBar = {
            AttendanceTopAppBar(
                onClickBackIcon = { if (!activity.isFinishing) activity.finish() },
                onClickRefreshIcon = viewModel::fetchAttendanceInfo
            )
        }
    ) { innerPaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = SoptTheme.colors.background)
                .padding(innerPaddingValues)
        ) {
            when (state) {
                AttendanceUiState.Loading -> {
                    AttendanceLoadingScreen()
                }

                is AttendanceUiState.Failure -> {}
                AttendanceUiState.NetworkError -> {}
                is AttendanceUiState.Success -> {
                    AttendanceScreen(
                        state = state as AttendanceUiState.Success,
                        onClickAttendance = viewModel::fetchCurrentRound,
                        onDismissAttendanceCodeDialog = viewModel::clearAttendanceCode,
                    )
                }
            }
        }
    }
}
