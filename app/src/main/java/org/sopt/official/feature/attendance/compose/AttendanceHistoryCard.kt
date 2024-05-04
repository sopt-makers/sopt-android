package org.sopt.official.feature.attendance.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.SoptTheme

@Composable
fun AttendanceHistoryCard(
    state: AttendanceHistoryCardState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
        modifier
            .background(color = SoptTheme.colors.onSurface800)
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_attendance_event_date),
                contentDescription = null,
                tint = SoptTheme.colors.onSurface300,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = state.eventDate, color = SoptTheme.colors.onSurface300)
        }
        Spacer(modifier = Modifier.height(7.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_attendance_event_location),
                contentDescription = null,
                tint = SoptTheme.colors.onSurface300,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = state.eventLocation, color = SoptTheme.colors.onSurface300)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = "오늘은 ", color = SoptTheme.colors.onSurface10)
            Text(text = state.eventName, color = SoptTheme.colors.onSurface10)
            Text(text = " 날이에요", color = SoptTheme.colors.onSurface10)
        }
        Spacer(modifier = Modifier.height(12.dp))
        AttendanceProgressBar(
            firstAttendance = state.firstAttendance,
            secondAttendance = state.secondAttendance,
            finalAttendance = state.finalAttendance,
        )
    }
}

class AttendanceHistoryCardState(
    val eventDate: String,
    val eventLocation: String,
    val eventName: String,
    val firstAttendance: MidtermAttendance,
    val secondAttendance: MidtermAttendance,
    val finalAttendance: FinalAttendance,
)

@Preview
@Composable
private fun AttendanceHistoryCardPreview() {
    SoptTheme {
        AttendanceHistoryCard(
            state =
            AttendanceHistoryCardState(
                eventDate = "3월 23일 토요일 14:00 - 18:00",
                eventLocation = "건국대학교 꽥꽥오리관",
                eventName = "2차 세미나",
                firstAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
                secondAttendance = MidtermAttendance.Absent,
                finalAttendance = FinalAttendance.LATE,
            ),
        )
    }
}
