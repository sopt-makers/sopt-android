package org.sopt.official.feature.attendance.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.model.AttendanceType
import org.sopt.official.feature.attendance.model.FinalAttendance
import org.sopt.official.feature.attendance.model.MidtermAttendance

@Composable
fun AttendanceProgressBar(
    firstAttendance: MidtermAttendance,
    secondAttendance: MidtermAttendance,
    finalAttendance: FinalAttendance,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        LinearProgressIndicator(
            progress = {
                calculateAttendanceProgress(
                    firstAttendanceState = firstAttendance,
                    secondAttendanceState = secondAttendance
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(top = 24.dp, start = 36.dp, end = 36.dp),
            color = SoptTheme.colors.onSurface10,
            trackColor = SoptTheme.colors.onSurface400,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MidtermAttendanceCard(
                midtermAttendance = firstAttendance,
            )
            MidtermAttendanceCard(
                midtermAttendance = secondAttendance,
            )
            FinalAttendanceCard(
                finalAttendance = finalAttendance,
            )
        }
    }
}

fun calculateAttendanceProgress(
    firstAttendanceState: MidtermAttendance,
    secondAttendanceState: MidtermAttendance
): Float {
    if (!firstAttendanceState.isFinished) return 0f
    if (!secondAttendanceState.isFinished) return 0.5f
    else return 1f
}

@Preview
@Composable
private fun AttendanceProgressBarNotYetPreview() {
    SoptTheme {
        AttendanceProgressBar(
            firstAttendance = MidtermAttendance.NotYet(AttendanceType.FIRST),
            secondAttendance = MidtermAttendance.NotYet(AttendanceType.SECOND),
            finalAttendance = FinalAttendance.NOT_YET,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800),
        )
    }
}

@Preview
@Composable
private fun AttendanceProgressBarLatePreview() {
    SoptTheme {
        AttendanceProgressBar(
            firstAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
            secondAttendance = MidtermAttendance.Absent,
            finalAttendance = FinalAttendance.LATE,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800),
        )
    }
}


@Preview
@Composable
private fun AttendanceProgressBarPresentPreview() {
    SoptTheme {
        AttendanceProgressBar(
            firstAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
            secondAttendance = MidtermAttendance.Present(attendanceAt = "16:00"),
            finalAttendance = FinalAttendance.PRESENT,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800),
        )
    }
}

@Preview
@Composable
private fun AttendanceProgressBarAbsentPreview() {
    SoptTheme {
        AttendanceProgressBar(
            firstAttendance = MidtermAttendance.Absent,
            secondAttendance = MidtermAttendance.Absent,
            finalAttendance = FinalAttendance.ABSENT,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800),
        )
    }
}
