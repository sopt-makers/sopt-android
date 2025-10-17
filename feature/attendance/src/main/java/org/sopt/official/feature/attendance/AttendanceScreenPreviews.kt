/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.Gray10
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.attendance.entity.AttendanceHistory
import org.sopt.official.domain.attendance.entity.AttendanceLog
import org.sopt.official.domain.attendance.entity.AttendanceStatus
import org.sopt.official.domain.attendance.entity.AttendanceSummary
import org.sopt.official.domain.attendance.entity.AttendanceUserInfo
import org.sopt.official.domain.attendance.entity.EventType
import org.sopt.official.domain.attendance.entity.EventAttribute
import org.sopt.official.domain.attendance.entity.SoptEvent
import org.sopt.official.feature.attendance.component.AttendanceButton
import org.sopt.official.feature.attendance.component.AttendanceEventInfoCard
import org.sopt.official.feature.attendance.component.AttendanceHistoryList
import org.sopt.official.feature.attendance.component.AttendanceTopBar
import org.sopt.official.feature.attendance.model.AttendanceButtonState
import org.sopt.official.feature.attendance.model.AttendanceConstants
import org.sopt.official.feature.attendance.model.ProgressBarUIState

// Sample data for previews
private val sampleSoptEvent = SoptEvent(
    id = 1,
    eventType = EventType.HAS_ATTENDANCE,
    date = "2024-01-15",
    location = "건국대학교 새천년관",
    eventName = "1차 세미나",
    message = "Android 기초 세미나입니다.",
    isAttendancePointAwardedEvent = true,
    attendances = listOf(
        SoptEvent.Attendance(
            status = AttendanceStatus.ATTENDANCE,
            attendedAt = "19:30"
        )
    )
)

private val sampleAttendanceHistory = AttendanceHistory(
    userInfo = AttendanceUserInfo(
        generation = 34,
        partName = "Android",
        userName = "김솝트",
        attendancePoint = 15
    ),
    attendanceSummary = AttendanceSummary(
        normal = 5,
        late = 2,
        abnormal = 1,
        participate = 1
    ),
    attendanceLog = listOf(
        AttendanceLog(
            attribute = EventAttribute.SEMINAR,
            attendanceState = "출석",
            eventName = "1차 세미나",
            date = "2024-01-15"
        ),
        AttendanceLog(
            attribute = EventAttribute.SEMINAR,
            attendanceState = "결석",
            eventName = "2차 세미나",
            date = "2024-01-22"
        )
    )
)

@Preview(name = "AttendanceScreen - Success State", showBackground = true)
@Composable
private fun AttendanceScreenSuccessPreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AttendanceTopBar(
                    onBackClick = { },
                    onRefreshClick = { }
                )

                AttendanceContentPreview(
                    soptEvent = sampleSoptEvent,
                    attendanceHistory = sampleAttendanceHistory,
                    buttonState = AttendanceButtonState.visible("1차 출석", true),
                    progressState = ProgressBarUIState(
                        isFirstProgressBarActive = true,
                        isFirstProgressBarAttendance = true,
                        isFirstToSecondLineActive = true
                    ),
                    onAttendanceClick = { },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(name = "AttendanceScreen - Loading State", showBackground = true)
@Composable
private fun AttendanceScreenLoadingPreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AttendanceTopBar(
                    onBackClick = { },
                    onRefreshClick = { }
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Gray10
                    )
                }
            }
        }
    }
}

@Preview(name = "AttendanceScreen - Error State", showBackground = true)
@Composable
private fun AttendanceScreenErrorPreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AttendanceTopBar(
                    onBackClick = { },
                    onRefreshClick = { }
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "출석 정보를 불러올 수 없습니다.",
                        color = Gray300,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(name = "AttendanceContent - Button Visible", showBackground = true)
@Composable
private fun AttendanceContentPreview() {
    SoptTheme {
        AttendanceContentPreview(
            soptEvent = sampleSoptEvent,
            attendanceHistory = sampleAttendanceHistory,
            buttonState = AttendanceButtonState.visible("1차 출석", true),
            progressState = ProgressBarUIState(
                isFirstProgressBarActive = true,
                isFirstProgressBarAttendance = true,
                isFirstToSecondLineActive = true
            ),
            onAttendanceClick = { }
        )
    }
}

@Preview(name = "AttendanceContent - Button Hidden", showBackground = true)
@Composable
private fun AttendanceContentNoButtonPreview() {
    SoptTheme {
        AttendanceContentPreview(
            soptEvent = sampleSoptEvent,
            attendanceHistory = sampleAttendanceHistory,
            buttonState = AttendanceButtonState.Hidden,
            progressState = ProgressBarUIState(
                isFirstProgressBarActive = true,
                isFirstProgressBarAttendance = true,
                isFirstToSecondLineActive = true,
                isSecondProgressBarActive = true,
                isSecondProgressBarAttendance = true,
                isSecondToThirdLineActive = true,
                isThirdProgressBarActive = true,
                isThirdProgressBarAttendance = true,
                isThirdProgressBarBeforeAttendance = true
            ),
            onAttendanceClick = { }
        )
    }
}

@Composable
private fun AttendanceContentPreview(
    soptEvent: SoptEvent,
    attendanceHistory: AttendanceHistory,
    buttonState: AttendanceButtonState,
    progressState: ProgressBarUIState,
    onAttendanceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize().background(Color.Black)) {
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
