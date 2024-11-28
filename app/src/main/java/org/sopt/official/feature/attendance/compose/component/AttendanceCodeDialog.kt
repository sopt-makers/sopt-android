package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.R
import org.sopt.official.designsystem.Black40
import org.sopt.official.designsystem.Gray60
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.attendance.model.AttendanceUiState.Success.AttendanceDayType.AttendanceDay.MidtermAttendance.NotYet.AttendanceSession

@Composable
fun AttendanceCodeDialog(
    codes: ImmutableList<String>,
    inputCodes: ImmutableList<String?>,
    attendanceSession: AttendanceSession,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier
                .background(
                    color = SoptTheme.colors.onSurface700,
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = stringResource(id = R.string.close),
                tint = SoptTheme.colors.onSurface10,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(onClick = onDismissRequest)
            )
            Text(
                text = stringResource(R.string.attendance_do, attendanceSession.type),
                style = SoptTheme.typography.heading18B,
                color = SoptTheme.colors.onSurface10
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.attendance_code_description),
                style = SoptTheme.typography.body13M,
                color = SoptTheme.colors.onSurface300
            )
            Spacer(modifier = Modifier.height(24.dp))
            AttendanceCodeCardList(
                codes = inputCodes,
                onTextChange = {},
                onTextFieldFull = {},
            )
            if (codes != inputCodes) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.attendance_code_does_not_match),
                    style = SoptTheme.typography.label12SB,
                    color = SoptTheme.colors.error
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(size = 6.dp),
                colors = ButtonColors(
                    containerColor = SoptTheme.colors.onSurface10,
                    contentColor = SoptTheme.colors.onSurface950,
                    disabledContainerColor = Black40,
                    disabledContentColor = Gray60,
                ),
                enabled = codes == inputCodes
            ) {
                Text(
                    text = stringResource(R.string.attendance_dialog_button),
                    style = SoptTheme.typography.body13M,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AttendanceCodeDialogPreview(
    @PreviewParameter(AttendanceCodeDialogPreviewParameterProvider::class) parameter: AttendanceCodeDialogPreviewParameter,
) {
    SoptTheme {
        AttendanceCodeDialog(
            codes = parameter.codes,
            inputCodes = parameter.inputCodes,
            attendanceSession = parameter.attendanceSession,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {}
        )
    }
}

data class AttendanceCodeDialogPreviewParameter(
    val codes: ImmutableList<String>,
    val inputCodes: ImmutableList<String?>,
    val attendanceSession: AttendanceSession,
)

class AttendanceCodeDialogPreviewParameterProvider :
    PreviewParameterProvider<AttendanceCodeDialogPreviewParameter> {
    override val values: Sequence<AttendanceCodeDialogPreviewParameter> =
        sequenceOf(
            AttendanceCodeDialogPreviewParameter(
                codes = persistentListOf("1", "2", "3", "4", "5"),
                inputCodes = persistentListOf("1", "2", "3", null, null),
                AttendanceSession.FIRST,
            ),
            AttendanceCodeDialogPreviewParameter(
                codes = persistentListOf("1", "2", "3", "4", "5"),
                inputCodes = persistentListOf("1", "2", "3", "4", "5"),
                AttendanceSession.FIRST,
            ),
            AttendanceCodeDialogPreviewParameter(
                codes = persistentListOf("1", "2", "3", "4", "5"),
                inputCodes = persistentListOf("1", "2", "3", null, null),
                AttendanceSession.SECOND,
            ),
            AttendanceCodeDialogPreviewParameter(
                codes = persistentListOf("1", "2", "3", "4", "5"),
                inputCodes = persistentListOf("1", "2", "3", "4", "5"),
                AttendanceSession.SECOND,
            ),
        )
}