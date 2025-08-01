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
package org.sopt.official.feature.attendance.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.attendance.entity.AttendanceHistory
import org.sopt.official.domain.attendance.entity.AttendanceLog
import org.sopt.official.domain.attendance.entity.AttendanceStatus
import org.sopt.official.domain.attendance.entity.AttendanceSummary
import org.sopt.official.domain.attendance.entity.AttendanceUserInfo
import org.sopt.official.domain.attendance.entity.EventType
import org.sopt.official.domain.attendance.entity.EventAttribute
import org.sopt.official.domain.attendance.entity.SoptEvent
import org.sopt.official.feature.attendance.model.AttendanceButtonState
import org.sopt.official.feature.attendance.model.AttendanceDialogState
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

@Preview(name = "AttendanceEventInfoCard - Normal", showBackground = true)
@Composable
private fun AttendanceEventInfoCardPreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(20.dp)
        ) {
            AttendanceEventInfoCard(
                soptEvent = sampleSoptEvent,
                progressState = ProgressBarUIState(
                    isFirstProgressBarActive = true,
                    isFirstProgressBarAttendance = true,
                    isFirstToSecondLineActive = true,
                    isSecondProgressBarActive = false
                )
            )
        }
    }
}

@Preview(name = "AttendanceEventInfoCard - Completed", showBackground = true)
@Composable
private fun AttendanceEventInfoCardCompletedPreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(20.dp)
        ) {
            AttendanceEventInfoCard(
                soptEvent = sampleSoptEvent.copy(
                    attendances = listOf(
                        SoptEvent.Attendance(AttendanceStatus.ATTENDANCE, "19:30"),
                        SoptEvent.Attendance(AttendanceStatus.ATTENDANCE, "21:00")
                    )
                ),
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
                )
            )
        }
    }
}

@Preview(name = "AttendanceButton - Enabled", showBackground = true)
@Composable
private fun AttendanceButtonEnabledPreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(20.dp)
        ) {
            AttendanceButton(
                text = "1차 출석",
                isEnabled = true,
                onClick = { }
            )
        }
    }
}

@Preview(name = "AttendanceButton - Disabled", showBackground = true)
@Composable
private fun AttendanceButtonDisabledPreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(20.dp)
        ) {
            AttendanceButton(
                text = "1차 출석 종료",
                isEnabled = false,
                onClick = { }
            )
        }
    }
}

@Preview(name = "AttendanceCodeDialog - Input", showBackground = true)
@Composable
private fun AttendanceCodeDialogPreview() {
    SoptTheme {
        AttendanceCodeDialog(
            title = "1차 세미나",
            onDismiss = { },
            onCodeSubmit = { },
            errorMessage = null
        )
    }
}

@Preview(name = "AttendanceCodeDialog - Error", showBackground = true)
@Composable
private fun AttendanceCodeDialogErrorPreview() {
    SoptTheme {
        AttendanceCodeDialog(
            title = "1차 세미나",
            onDismiss = { },
            onCodeSubmit = { },
            errorMessage = "코드가 일치하지 않아요!"
        )
    }
}

@Preview(name = "AttendanceHistoryList", showBackground = true)
@Composable
private fun AttendanceHistoryListPreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(20.dp)
        ) {
            AttendanceHistoryList(
                attendanceHistory = sampleAttendanceHistory
            )
        }
    }
}

@Preview(name = "AttendanceTopBar", showBackground = true)
@Composable
private fun AttendanceTopBarPreview() {
    SoptTheme {
        AttendanceTopBar(
            onBackClick = { },
            onRefreshClick = { }
        )
    }
}

@Preview(name = "AttendanceRoundProgressBar - First Active", showBackground = true)
@Composable
private fun AttendanceRoundProgressBarFirstActivePreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(20.dp)
        ) {
            AttendanceProgressIndicator(
                progressState = ProgressBarUIState(
                    isFirstProgressBarActive = true,
                    isFirstProgressBarAttendance = true,
                    isFirstToSecondLineActive = true
                )
            )
        }
    }
}

@Preview(name = "AttendanceRoundProgressBar - All Complete", showBackground = true)
@Composable
private fun AttendanceRoundProgressBarCompletePreview() {
    SoptTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(20.dp)
        ) {
            AttendanceProgressIndicator(
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
                )
            )
        }
    }
}
