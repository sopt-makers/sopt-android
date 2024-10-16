package org.sopt.official.feature.attendance.compose.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.model.FinalAttendance
import org.sopt.official.feature.attendance.model.MidtermAttendance
import org.sopt.official.feature.attendance.model.state.AttendanceProgressBarState

@Composable
fun TodayAttendanceCard(
    eventDate: String,
    eventLocation: String,
    eventName: String,
    firstAttendance: MidtermAttendance,
    secondAttendance: MidtermAttendance,
    finalAttendance: FinalAttendance,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_attendance_event_date),
                contentDescription = null,
                tint = SoptTheme.colors.onSurface300,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = eventDate,
                color = SoptTheme.colors.onSurface300,
                style = SoptTheme.typography.body14M
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_attendance_event_location),
                contentDescription = null,
                tint = SoptTheme.colors.onSurface300,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = eventLocation,
                color = SoptTheme.colors.onSurface300,
                style = SoptTheme.typography.body14M
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(
                text = stringResource(R.string.attendance_event_info_prefix),
                color = SoptTheme.colors.onSurface10,
                style = SoptTheme.typography.body18M
            )
            Text(
                text = eventName,
                color = SoptTheme.colors.onSurface10,
                style = SoptTheme.typography.body18M.copy(fontWeight = FontWeight.ExtraBold)
            )
            Text(
                text = stringResource(R.string.attendance_event_info_suffix),
                color = SoptTheme.colors.onSurface10,
                style = SoptTheme.typography.body18M
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        AttendanceProgressBar(
            state = AttendanceProgressBarState(
                firstAttendance = firstAttendance,
                secondAttendance = secondAttendance,
                finalAttendance = finalAttendance,
            )
        )
    }
}

@Preview
@Composable
private fun TodayAttendanceCardPreview() {
    SoptTheme {
        Column(
            modifier = Modifier.background(color = SoptTheme.colors.background)
        ) {
            TodayAttendanceCard(
                eventDate = "3월 23일 토요일 14:00 - 18:00",
                eventLocation = "건국대학교 꽥꽥오리관",
                eventName = "2차 세미나",
                firstAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
                secondAttendance = MidtermAttendance.Absent,
                finalAttendance = FinalAttendance.LATE,
            )
        }
    }
}
