package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme

@Composable
fun AttendanceHistoryUserInfoCard(
    userTitle: String,
    attendanceScore: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = userTitle,
            color = SoptTheme.colors.onSurface300,
            style = SoptTheme.typography.body14M
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "현재 출석점수는 ",
                color = SoptTheme.colors.onSurface10,
                style = SoptTheme.typography.body18M
            )
            Text(
                text = "${attendanceScore}점",
                color = Orange400,
                style = SoptTheme.typography.title20SB
            )
            Text(
                text = " 입니다!",
                color = SoptTheme.colors.onSurface10,
                style = SoptTheme.typography.body18M
            )
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