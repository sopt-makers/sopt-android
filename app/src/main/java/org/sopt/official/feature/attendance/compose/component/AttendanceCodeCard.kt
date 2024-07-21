package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun AttendanceCodeCard(
    text: String,
    onTextChange: (String) -> Unit,
    onTextFieldFull: () -> Unit,
    modifier: Modifier = Modifier,
    textMaxLength: Int = 1,
) {
    BasicTextField(
        value = text,
        onValueChange = { newText: String ->
            if (newText.length < textMaxLength) {
                onTextChange(newText)
            } else {
                onTextFieldFull()
            }
        }, modifier = modifier
            .background(
                color = if (text.isEmpty()) SoptTheme.colors.onSurface600
                else SoptTheme.colors.onSurface800,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (text.isEmpty()) SoptTheme.colors.onSurface500
                else SoptTheme.colors.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 17.dp, vertical = 18.dp)
            .width(10.dp),
        textStyle = SoptTheme.typography.heading16B.copy(color = SoptTheme.colors.primary)
    )
}

@Preview
@Composable
private fun AttendanceCodeCardPreview(
    @PreviewParameter(AttendanceCodeCardPreviewParameterProvider::class) text: String,
) {
    SoptTheme {
        AttendanceCodeCard(
            text = text,
            onTextChange = {},
            onTextFieldFull = {}
        )
    }
}

private class AttendanceCodeCardPreviewParameterProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf("", "8")
}
