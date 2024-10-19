package org.sopt.official.feature.attendance.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.sopt.official.R
import org.sopt.official.designsystem.Black40
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.compose.component.AttendanceHistoryCard
import org.sopt.official.feature.attendance.compose.component.AttendanceTopAppBar
import org.sopt.official.feature.attendance.compose.component.TodayAttendanceCard
import org.sopt.official.feature.attendance.compose.component.TodayNoAttendanceCard
import org.sopt.official.feature.attendance.compose.component.TodayNoScheduleCard
import org.sopt.official.feature.attendance.model.AttendanceAction
import org.sopt.official.feature.attendance.model.AttendanceDayType
import org.sopt.official.feature.attendance.model.AttendanceHistory
import org.sopt.official.feature.attendance.model.AttendanceResultType
import org.sopt.official.feature.attendance.model.AttendanceUiState
import org.sopt.official.feature.attendance.model.FinalAttendance
import org.sopt.official.feature.attendance.model.MidtermAttendance

@Composable
fun AttendanceScreen(state: AttendanceUiState.Success, action: AttendanceAction) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            when (state.attendanceDayType) {
                is AttendanceDayType.AttendanceDay -> {
                    TodayAttendanceCard(
                        modifier = Modifier.fillMaxWidth(),
                        eventDate = state.attendanceDayType.eventDate,
                        eventLocation = state.attendanceDayType.eventLocation,
                        eventName = state.attendanceDayType.eventName,
                        firstAttendance = state.attendanceDayType.firstAttendance,
                        secondAttendance = state.attendanceDayType.secondAttendance,
                        finalAttendance = state.attendanceDayType.finalAttendance,
                    )
                }

                is AttendanceDayType.Event -> {
                    TodayNoAttendanceCard(
                        modifier = Modifier.fillMaxWidth(),
                        eventDate = state.attendanceDayType.eventDate,
                        eventLocation = state.attendanceDayType.eventLocation,
                        eventName = state.attendanceDayType.eventLocation,
                    )
                }

                AttendanceDayType.NONE -> {
                    TodayNoScheduleCard(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            AttendanceHistoryCard(
                userTitle = state.userTitle,
                attendanceScore = state.attendanceScore,
                totalAttendanceResult = state.totalAttendanceResult,
                attendanceHistoryList = state.attendanceHistoryList,
                scrollState = scrollState,
            )
            Spacer(Modifier.height(36.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x000F1010), Color(0xFF0F1010)
                        )
                    )
                )
                .padding(bottom = 41.dp)
        )
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 9.dp),
            shape = RoundedCornerShape(size = 6.dp),
            colors = ButtonColors(
                containerColor = SoptTheme.colors.onSurface10,
                contentColor = SoptTheme.colors.onSurface950,
                disabledContainerColor = Black40,
                disabledContentColor = Gray60,
            ),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.attendance_dialog_button),
                style = SoptTheme.typography.label18SB,
            )
        }
    }
}

@Preview
@Composable
private fun AttendanceScreenPreview(@PreviewParameter(AttendanceScreenPreviewParameterProvider::class) parameter: AttendanceScreenPreviewParameter) {
    SoptTheme {
        Scaffold(
            topBar = {
                AttendanceTopAppBar(
                    onClickBackIcon = { },
                    onClickRefreshIcon = { }
                )
            }
        ) { innerPaddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = SoptTheme.colors.background)
                    .padding(innerPaddingValues)
            ) {
                AttendanceScreen(
                    state = AttendanceUiState.Success(
                        attendanceDayType = parameter.attendanceDayType,
                        userTitle = "32기 디자인파트 김솝트",
                        attendanceScore = 1,
                        totalAttendanceResult = persistentMapOf(
                            Pair(AttendanceResultType.ALL, 16),
                            Pair(AttendanceResultType.PRESENT, 10),
                            Pair(AttendanceResultType.LATE, 4),
                            Pair(AttendanceResultType.ABSENT, 2)
                        ),
                        attendanceHistoryList = persistentListOf(
                            AttendanceHistory(
                                status = "출석", eventName = "1차 세미나", date = "00월 00일"
                            ),
                            AttendanceHistory(
                                status = "출석", eventName = "2차 세미나", date = "00월 00일"
                            ),
                            AttendanceHistory(
                                status = "출석", eventName = "3차 세미나", date = "00월 00일"
                            ),
                            AttendanceHistory(
                                status = "출석", eventName = "4차 세미나", date = "00월 00일"
                            ),
                            AttendanceHistory(
                                status = "출석", eventName = "5차 세미나", date = "00월 00일"
                            ),
                            AttendanceHistory(
                                status = "출석", eventName = "6차 세미나", date = "00월 00일"
                            ),
                        ),
                    ),
                    action = AttendanceAction(onFakeClick = {})
                )
            }
        }
    }
}

data class AttendanceScreenPreviewParameter(
    val attendanceDayType: AttendanceDayType,
)


class AttendanceScreenPreviewParameterProvider() :
    PreviewParameterProvider<AttendanceScreenPreviewParameter> {

    override val values: Sequence<AttendanceScreenPreviewParameter> = sequenceOf(
        AttendanceScreenPreviewParameter(
            attendanceDayType = AttendanceDayType.AttendanceDay(
                eventDate = "3월 23일 토요일 14:00 - 18:00",
                eventLocation = "건국대학교 꽥꽥오리관",
                eventName = "2차 세미나",
                firstAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
                secondAttendance = MidtermAttendance.Absent,
                finalAttendance = FinalAttendance.LATE,
            )
        ), AttendanceScreenPreviewParameter(
            attendanceDayType = AttendanceDayType.Event(
                eventDate = "3월 23일 토요일 14:00 - 18:00",
                eventLocation = "건국대학교 꽥꽥오리관",
                eventName = "2차 세미나",
            )
        ),
        AttendanceScreenPreviewParameter(
            attendanceDayType = AttendanceDayType.NONE
        )
    )
}