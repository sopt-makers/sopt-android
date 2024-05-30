package org.sopt.official.feature.attendance.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.compose.component.AttendanceCountCard
import org.sopt.official.feature.attendance.compose.component.AttendanceHistoryUserInfoCard
import org.sopt.official.feature.attendance.model.AttendanceResultType
import org.sopt.official.feature.attendance.model.state.AttendanceHistoryCardState

@Composable
fun AttendanceHistoryCard(state: AttendanceHistoryCardState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(color = SoptTheme.colors.onSurface800)
            .padding(all = 32.dp)
    ) {
        AttendanceHistoryUserInfoCard(
            userTitle = state.userTitle,
            attendanceScore = state.attendanceScore
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = SoptTheme.colors.onSurface700, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            state.totalAttendanceResult.map { attendanceResult ->
                AttendanceCountCard(
                    resultType = attendanceResult.key.type,
                    count = attendanceResult.value
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun AttendanceHistoryCardPreview() {
    SoptTheme {
        AttendanceHistoryCard(
            state = AttendanceHistoryCardState(
                userTitle = "32기 디자인파트 김솝트",
                attendanceScore = 1,
                totalAttendanceResult = mapOf(
                    Pair(AttendanceResultType.ALL, 16),
                    Pair(AttendanceResultType.PRESENT, 5),
                    Pair(AttendanceResultType.LATE, 0),
                    Pair(AttendanceResultType.ABSENT, 11)
                )
            )
        )
    }
}