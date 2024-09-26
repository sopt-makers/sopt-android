package org.sopt.official.feature.attendance.compose.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun AttendanceCodeCardList(
    codes: List<String?>,
    onTextChange: (newText: String) -> Unit,
    onTextFieldFull: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        repeat(codes.size) { index ->
            AttendanceCodeCard(
                text = codes[index] ?: "",
                onTextChange = onTextChange,
                onTextFieldFull = onTextFieldFull
            )
            if (index < codes.size) {
                Spacer(modifier = Modifier.width(width = 12.dp))
            }
        }
    }
}

@Preview
@Composable
private fun AttendanceCodeCardListPreview() {
    SoptTheme {
        AttendanceCodeCardList(
            codes = listOf("8", "8", "8", null, null),
            onTextChange = {},
            onTextFieldFull = {})
    }
}