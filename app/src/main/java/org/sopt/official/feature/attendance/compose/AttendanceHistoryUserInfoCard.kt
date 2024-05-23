package org.sopt.official.feature.attendance.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.SoptTheme

@Composable
fun AttendanceHistoryUserInfoCard(userTitle: String, attendanceScore: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = userTitle, color = SoptTheme.colors.onSurface300)
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(text = "현재 출석점수는 ", color = SoptTheme.colors.onSurface10)
            Text(text = "${attendanceScore}점", color = SoptTheme.colors.onSurface10)
            Text(text = " 입니다!", color = SoptTheme.colors.onSurface10)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.padding(end = 2.dp),
                painter = painterResource(id = R.drawable.ic_attendance_point_info),
                contentDescription = null,
                tint = SoptTheme.colors.onSurface10
            )
        }
    }
}

@Preview
@Composable
fun AttendanceHistoryUserInfoCardPreview() {
    SoptTheme {
        AttendanceHistoryUserInfoCard(
            userTitle = "32기 디자인파트 김솝트",
            attendanceScore = 1,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800)
        )
    }
}