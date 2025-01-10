package org.sopt.official.feature.attendance.compose.component

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
import org.sopt.official.feature.attendance.model.FinalAttendance
import org.sopt.official.feature.attendance.model.MidtermAttendance
import org.sopt.official.feature.attendance.model.MidtermAttendance.NotYet.AttendanceSession

@Composable
fun AttendanceProgressBar(
    firstAttendance: MidtermAttendance,
    secondAttendance: MidtermAttendance,
    finalAttendance: FinalAttendance,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LinearProgressIndicator(
            progress = {
                calculateAttendanceProgress(
                    firstAttendance = firstAttendance,
                    secondAttendance = secondAttendance
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
    firstAttendance: MidtermAttendance,
    secondAttendance: MidtermAttendance,
): Float {
    if (!firstAttendance.isFinished) return 0f
    if (!secondAttendance.isFinished) {
        return 0.5f
    } else {
        return 1f
    }
}


class AttendanceProgressBarPreviewParameter(
    val firstAttendance: MidtermAttendance,
    val secondAttendance: MidtermAttendance,
    val finalAttendance: FinalAttendance,
)

class AttendanceProgressBarPreviewParameterProvider(
    override val values: Sequence<AttendanceProgressBarPreviewParameter> = sequenceOf(
        AttendanceProgressBarPreviewParameter(
            firstAttendance = MidtermAttendance.NotYet(AttendanceSession.FIRST),
            secondAttendance = MidtermAttendance.NotYet(AttendanceSession.SECOND),
            finalAttendance = FinalAttendance.NOT_YET,
        ),
        AttendanceProgressBarPreviewParameter(
            firstAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
            secondAttendance = MidtermAttendance.Absent,
            finalAttendance = FinalAttendance.LATE,
        ),
        AttendanceProgressBarPreviewParameter(
            firstAttendance = MidtermAttendance.Present(attendanceAt = "14:00"),
            secondAttendance = MidtermAttendance.Present(attendanceAt = "16:00"),
            finalAttendance = FinalAttendance.PRESENT,
        ),
        AttendanceProgressBarPreviewParameter(
            firstAttendance = MidtermAttendance.Absent,
            secondAttendance = MidtermAttendance.Absent,
            finalAttendance = FinalAttendance.ABSENT,
        ),
    ),
) : PreviewParameterProvider<AttendanceProgressBarPreviewParameter>

@Preview(showBackground = true)
@Composable
private fun AttendanceProgressBarPreview(
    @PreviewParameter(AttendanceProgressBarPreviewParameterProvider::class) attendanceProgressBarPreviewParameter: AttendanceProgressBarPreviewParameter,
) {
    SoptTheme {
        AttendanceProgressBar(
            firstAttendance = attendanceProgressBarPreviewParameter.firstAttendance,
            secondAttendance = attendanceProgressBarPreviewParameter.secondAttendance,
            finalAttendance = attendanceProgressBarPreviewParameter.finalAttendance
        )
    }
}
