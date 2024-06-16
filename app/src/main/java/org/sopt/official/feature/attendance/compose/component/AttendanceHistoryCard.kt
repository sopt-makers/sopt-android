package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.common.collect.ImmutableList
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.model.AttendanceHistory
import org.sopt.official.feature.attendance.model.AttendanceResultType

@Composable
fun AttendanceHistoryCard(
    userTitle: String,
    attendanceScore: Int,
    totalAttendanceResult: Map<AttendanceResultType, Int>,
    attendanceHistoryList: ImmutableList<AttendanceHistory>,
    scrollState: ScrollState, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color = SoptTheme.colors.onSurface800)
            .padding(all = 32.dp)
    ) {
        AttendanceHistoryUserInfoCard(
            userTitle = userTitle,
            attendanceScore = attendanceScore
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = SoptTheme.colors.onSurface700, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            totalAttendanceResult.forEach { attendanceResult ->
                AttendanceCountCard(
                    resultType = attendanceResult.key.type,
                    count = attendanceResult.value
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        AttendanceHistoryListCard(
            attendanceHistoryList = attendanceHistoryList,
            scrollState = scrollState,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun AttendanceHistoryCardPreview() {
    SoptTheme {
        AttendanceHistoryCard(
            userTitle = "32기 디자인파트 김솝트",
            attendanceScore = 1,
            totalAttendanceResult = mapOf(
                Pair(AttendanceResultType.ALL, 16),
                Pair(AttendanceResultType.PRESENT, 5),
                Pair(AttendanceResultType.LATE, 0),
                Pair(AttendanceResultType.ABSENT, 11)
            ),
            attendanceHistoryList = ImmutableList.of(
                AttendanceHistory(
                    status = "출석", eventName = "1차 세미나", date = "00월 00일"
                ),
                AttendanceHistory(
                    status = "출석", eventName = "1차 세미나", date = "00월 00일"
                ),
                AttendanceHistory(
                    status = "출석", eventName = "1차 세미나", date = "00월 00일"
                ),
            ),
            scrollState = rememberScrollState()
        )
    }
}