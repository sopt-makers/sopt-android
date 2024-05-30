package org.sopt.official.feature.attendance.compose

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.model.AttendanceType
import org.sopt.official.feature.attendance.model.FinalAttendance
import org.sopt.official.feature.attendance.model.MidtermAttendance
import org.sopt.official.feature.attendance.model.state.AttendanceProgressBarState

@Composable
fun AttendanceProgressBar(
    state: AttendanceProgressBarState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        LinearProgressIndicator(
            progress = {
                calculateAttendanceProgress(
                    firstAttendance = state.firstAttendance,
                    secondAttendance = state.secondAttendance
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
                midtermAttendance = state.firstAttendance,
            )
            MidtermAttendanceCard(
                midtermAttendance = state.secondAttendance,
            )
            FinalAttendanceCard(
                finalAttendance = state.finalAttendance,
            )
        }
    }
}

fun calculateAttendanceProgress(
    firstAttendance: MidtermAttendance,
    secondAttendance: MidtermAttendance
): Float {
    if (!firstAttendance.isFinished) return 0f
    if (!secondAttendance.isFinished) return 0.5f
    else return 1f
}

class AttendanceProgressBarPreviewParameterProvider(
    override val values: Sequence<AttendanceProgressBarState> = sequenceOf(
        AttendanceProgressBarState(
            firstAttendance = MidtermAttendance.NotYet(AttendanceType.FIRST),
            secondAttendance = MidtermAttendance.NotYet(AttendanceType.SECOND),
            finalAttendance = FinalAttendance.NOT_YET,
        ),
        AttendanceProgressBarState(
            firstAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
            secondAttendance = MidtermAttendance.Absent,
            finalAttendance = FinalAttendance.LATE,
        ),
        AttendanceProgressBarState(
            firstAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
            secondAttendance = MidtermAttendance.Present(attendanceAt = "16:00"),
            finalAttendance = FinalAttendance.PRESENT,
        ),
        AttendanceProgressBarState(
            firstAttendance = MidtermAttendance.Absent,
            secondAttendance = MidtermAttendance.Absent,
            finalAttendance = FinalAttendance.ABSENT,
        ),
    )
) : PreviewParameterProvider<AttendanceProgressBarState>

@Preview(showBackground = true)
@Composable
private fun AttendanceProgressBarPreview(
    @PreviewParameter(AttendanceProgressBarPreviewParameterProvider::class) attendanceProgressBarState: AttendanceProgressBarState
) {
    SoptTheme {
        AttendanceProgressBar(state = attendanceProgressBarState)
    }
}