package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.SoptTheme

@Composable
fun AttendanceCountCard(resultType: String, count: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = resultType,
            color = SoptTheme.colors.onSurface300,
            style = SoptTheme.typography.label12SB
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = count.toFormattedNumber(),
            color = SoptTheme.colors.onSurface50,
            style = SoptTheme.typography.body14M
        )
    }
}

fun Int.toFormattedNumber(): String = "%02d회".format(this)

@Preview
@Composable
private fun AttendanceCountCardPreview() {
    SoptTheme {
        Row {
            AttendanceCountCard(
                resultType = stringResource(id = R.string.title_attendance_count_all),
                count = 0
            )
            Spacer(Modifier.width(10.dp))
            AttendanceCountCard(
                resultType = stringResource(id = R.string.title_attendance_count_normal),
                count = 0
            )
            Spacer(Modifier.width(10.dp))
            AttendanceCountCard(
                resultType = stringResource(id = R.string.title_attendance_count_late),
                count = 0
            )
            Spacer(Modifier.width(10.dp))
            AttendanceCountCard(
                resultType = stringResource(id = R.string.title_attendance_count_abnormal),
                count = 0
            )
        }
    }
}