package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun TodayNoAttendanceCard(
    eventDate: String,
    eventLocation: String,
    eventName: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
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
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.text_not_awarded),
            color = SoptTheme.colors.onSurface100,
            style = SoptTheme.typography.body14M,
        )
    }
}

@Preview
@Composable
private fun TodayNoAttendanceCardPreview() {
    SoptTheme {
        Column(modifier = Modifier.background(color = SoptTheme.colors.background)) {
            TodayNoAttendanceCard(
                eventDate = "5월 12일 일요일 14:00 - 18:00",
                eventLocation = "배달의민족주문~",
                eventName = "데모데이",
            )
        }
    }
}
