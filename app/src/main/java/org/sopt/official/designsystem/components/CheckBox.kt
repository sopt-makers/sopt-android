package org.sopt.official.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.R
import org.sopt.official.designsystem.style.SoptTheme

@Composable
fun CheckBox(
    containerModifier: Modifier = Modifier.size(48.dp),
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Box(
        modifier = containerModifier
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = if (checked) SoptTheme.colors.primary else SoptTheme.colors.onSurface20,
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "체크 아이콘"
            )
        }
    }
}

@Preview
@Composable
fun CheckedCheckboxPreview() {
    SoptTheme {
        CheckBox(
            checked = true,
            onCheckedChange = {}
        )
    }
}

@Preview
@Composable
fun UncheckedCheckboxPreview() {
    SoptTheme {
        CheckBox(
            checked = false,
            onCheckedChange = {}
        )
    }
}
