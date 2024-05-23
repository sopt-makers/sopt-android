package org.sopt.official.feature.attendance.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun AttendanceHistoryCard(state: AttendanceHistoryCardState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(color = SoptTheme.colors.onSurface800)
            .padding(vertical = 32.dp, horizontal = 24.dp)
    ) {
        AttendanceHistoryUserInfoCard(
            userTitle = state.userTitle,
            attendanceScore = state.attendanceScore
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

class AttendanceHistoryCardState(
    val userTitle: String,
    val attendanceScore: Int,
)

@Preview
@Composable
private fun AttendanceHistoryCardPreview() {
    SoptTheme {
        AttendanceHistoryCard(
            state = AttendanceHistoryCardState(
                userTitle = "32기 디자인파트 김솝트",
                attendanceScore = 1
            )
        )
    }
}