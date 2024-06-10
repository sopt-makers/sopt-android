package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.model.AttendanceHistory

@Composable
fun AttendanceHistoryListCard(
    attendanceHistoryList: List<AttendanceHistory>,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "나의 출결 현황",
            color = SoptTheme.colors.onSurface300,
            style = SoptTheme.typography.body14M,
        )
        Spacer(Modifier.height(25.dp))
        Column(
            Modifier.scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            )
        ) {
            attendanceHistoryList.forEachIndexed { index, attendanceHistory ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = attendanceHistory.status,
                        style = SoptTheme.typography.body14R,
                        color = SoptTheme.colors.onSurface100,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = attendanceHistory.eventName,
                        style = SoptTheme.typography.label16SB,
                        color = SoptTheme.colors.onSurface10,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = attendanceHistory.date,
                        style = SoptTheme.typography.body14R,
                        color = SoptTheme.colors.onSurface100,
                    )
                }
                if (index < attendanceHistoryList.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AttendanceHistoryListCardPreview() {
    SoptTheme {
        AttendanceHistoryListCard(
            attendanceHistoryList = listOf(
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
