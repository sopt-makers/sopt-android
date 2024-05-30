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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.model.FinalAttendance

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
            style = SoptTheme.typography.label14SB
        )
    }
}

class FinalAttendanceCardPreviewParameterProvider(
    override val values: Sequence<FinalAttendance> = sequenceOf(
        FinalAttendance.NOT_YET,
        FinalAttendance.PRESENT,
        FinalAttendance.LATE,
        FinalAttendance.ABSENT,
    )
) : PreviewParameterProvider<FinalAttendance>

@Preview
@Composable
private fun FinalAttendanceCardPreview(
    @PreviewParameter(FinalAttendanceCardPreviewParameterProvider::class) finalAttendance: FinalAttendance
) {
    SoptTheme {
        FinalAttendanceCard(
            finalAttendance = finalAttendance,
            modifier = Modifier.background(color = SoptTheme.colors.onSurface800)
        )
    }
}
