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
fun FinalAttendanceCard(
    finalAttendance: FinalAttendance,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(finalAttendance.imageResId),
            contentDescription = null,
        )
        Text(
            text = finalAttendance.result,
            color = if (finalAttendance.isFinished) SoptTheme.colors.onSurface10 else SoptTheme.colors.onSurface500,
        )
    }
}

enum class FinalAttendance(
    @DrawableRes val imageResId: Int, val isFinished: Boolean, val result: String,
) {
    NOT_YET(
        imageResId = R.drawable.ic_attendance_state_nothing,
        isFinished = false,
        result = "출석 전"
    ),
    PRESENT(
        imageResId = R.drawable.ic_attendance_state_done,
        isFinished = true,
        result = "출석완료!"
    ),
    LATE(
        imageResId = R.drawable.ic_attendance_state_late,
        isFinished = true,
        result = "지각"
    ),
    ABSENT(
        imageResId = R.drawable.ic_attendance_state_absence_black,
        isFinished = true,
        result = "결석"
    )
}

@Preview
@Composable
private fun AttendanceProgressCheckCardNotYetPreview() {
    SoptTheme {
        FinalAttendanceCard(
            finalAttendance = FinalAttendance.NOT_YET,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800)
        )
    }
}

@Preview
@Composable
private fun AttendanceProgressCheckCardPresentPreview() {
    SoptTheme {
        FinalAttendanceCard(
            finalAttendance = FinalAttendance.PRESENT,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800)
        )
    }
}

@Preview
@Composable
private fun AttendanceProgressCheckCardLatePreview() {
    SoptTheme {
        FinalAttendanceCard(
            finalAttendance = FinalAttendance.LATE,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800)
        )
    }
}

@Preview
@Composable
private fun AttendanceProgressCheckCardAbsentPreview() {
    SoptTheme {
        FinalAttendanceCard(
            finalAttendance = FinalAttendance.ABSENT,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800)
        )
    }
}
