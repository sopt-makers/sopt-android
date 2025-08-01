/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray300
import org.sopt.official.domain.attendance.entity.SoptEvent
import org.sopt.official.feature.attendance.component.AttendanceButton
import org.sopt.official.feature.attendance.component.AttendanceCodeDialog
import org.sopt.official.feature.attendance.component.AttendanceEventInfoCard
import org.sopt.official.feature.attendance.component.AttendanceHistoryList
import org.sopt.official.feature.attendance.component.AttendanceTopBar
import org.sopt.official.feature.attendance.model.AttendanceConstants
import org.sopt.official.feature.attendance.model.AttendanceDialogState

@Composable
fun AttendanceScreen(
    onBackClick: () -> Unit,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAttendanceData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AttendanceTopBar(
                onBackClick = onBackClick,
                onRefreshClick = { viewModel.loadAttendanceData() }
            )

            when {
                uiState.isDataReady -> {
                    AttendanceContent(
                        soptEvent = uiState.soptEvent!!,
                        attendanceHistory = uiState.attendanceHistory!!,
                        buttonState = uiState.buttonState,
                        progressState = uiState.progressState,
                        onAttendanceClick = { 
                            viewModel.showAttendanceDialog()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Gray10
                        )
                    }
                }
                uiState.hasError -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.error ?: "출석 정보를 불러올 수 없습니다.",
                            color = Gray300,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        when (val currentDialogState = dialogState) {
            is AttendanceDialogState.CodeInput -> {
                AttendanceCodeDialog(
                    title = currentDialogState.title,
                    onDismiss = { viewModel.closeDialog() },
                    onCodeSubmit = { code -> viewModel.checkAttendanceCode(code) },
                    errorMessage = null
                )
            }
            is AttendanceDialogState.Error -> {
                AttendanceCodeDialog(
                    title = currentDialogState.title,
                    onDismiss = { viewModel.closeDialog() },
                    onCodeSubmit = { code -> viewModel.checkAttendanceCode(code) },
                    errorMessage = currentDialogState.message
                )
            }
            is AttendanceDialogState.Hidden -> {
                // No dialog
            }
        }
    }
}

@Composable
private fun AttendanceContent(
    soptEvent: SoptEvent,
    attendanceHistory: org.sopt.official.domain.attendance.entity.AttendanceHistory,
    buttonState: org.sopt.official.feature.attendance.model.AttendanceButtonState,
    progressState: org.sopt.official.feature.attendance.model.ProgressBarUIState,
    onAttendanceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = AttendanceConstants.BOTTOM_PADDING_DP.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                AttendanceEventInfoCard(
                    soptEvent = soptEvent,
                    progressState = progressState
                )
            }

            item {
                AttendanceHistoryList(
                    attendanceHistory = attendanceHistory
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AttendanceConstants.GRADIENT_HEIGHT_DP.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        )
                    )
                )
        )

        if (buttonState.isVisible) {
            AttendanceButton(
                text = buttonState.text,
                isEnabled = buttonState.isEnabled,
                onClick = onAttendanceClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }
    }
}
