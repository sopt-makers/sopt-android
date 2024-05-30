package org.sopt.official.feature.attendance.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.model.AttendanceType
import org.sopt.official.feature.attendance.model.MidtermAttendance

@Composable
fun MidtermAttendanceCard(
    midtermAttendance: MidtermAttendance,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(midtermAttendance.imageResId),
            contentDescription = null,
        )
        Text(
            text = midtermAttendance.description,
            color = if (midtermAttendance.isFinished) SoptTheme.colors.onSurface10 else SoptTheme.colors.onSurface500,
            style = SoptTheme.typography.label14SB
        )
    }
}

@Preview
@Composable
private fun AttendanceProgressCheckCardNotYetPreview() {
    SoptTheme {
        MidtermAttendanceCard(
            midtermAttendance = MidtermAttendance.NotYet(attendanceType = AttendanceType.FIRST),
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800)
        )
    }
}

@Preview
@Composable
private fun AttendanceProgressCheckCardPresentPreview() {
    SoptTheme {
        MidtermAttendanceCard(
            midtermAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800)
        )
    }
}

@Preview
@Composable
private fun AttendanceProgressCheckCardAbsentPreview() {
    SoptTheme {
        MidtermAttendanceCard(
            midtermAttendance = MidtermAttendance.Absent,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800)
        )
    }
}
