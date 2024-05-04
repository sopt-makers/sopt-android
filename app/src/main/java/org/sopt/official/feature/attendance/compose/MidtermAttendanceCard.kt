package org.sopt.official.feature.attendance.compose

import androidx.annotation.DrawableRes
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
import org.sopt.official.R
import org.sopt.official.designsystem.SoptTheme

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
        )
    }
}

enum class AttendanceType(val type: String) {
    FIRST("1차 출석"),
    SECOND("2차 출석")
}

sealed class MidtermAttendance(
    @DrawableRes val imageResId: Int,
    val isFinished: Boolean,
    val description: String
) {
    class NotYet(attendanceType: AttendanceType) : MidtermAttendance(
        imageResId = R.drawable.ic_attendance_state_nothing,
        isFinished = false,
        description = attendanceType.type
    )

    class Present(attendanceAt: String) : MidtermAttendance(
        imageResId = R.drawable.ic_attendance_state_yes,
        isFinished = true,
        description = attendanceAt
    )

    object Absent : MidtermAttendance(
        imageResId = R.drawable.ic_attendance_state_absence_white,
        isFinished = true,
        description = "-"
    )
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
