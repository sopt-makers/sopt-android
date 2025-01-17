package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.model.AttendanceSession
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

class MidtermAttendanceCardPreviewParameterProvider(
    override val values: Sequence<MidtermAttendance> = sequenceOf(
        MidtermAttendance.NotYet(attendanceSession = AttendanceSession.FIRST),
        MidtermAttendance.Present(attendanceAt = "14:00"),
        MidtermAttendance.Absent,
    ),
) : PreviewParameterProvider<MidtermAttendance>

@Preview(showBackground = true)
@Composable
private fun MidtermAttendanceCardPreview(
    @PreviewParameter(MidtermAttendanceCardPreviewParameterProvider::class) midtermAttendance: MidtermAttendance,
) {
    SoptTheme {
        MidtermAttendanceCard(
            midtermAttendance = midtermAttendance
        )
    }
}
