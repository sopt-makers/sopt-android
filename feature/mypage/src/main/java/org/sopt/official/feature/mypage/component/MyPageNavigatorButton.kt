package org.sopt.official.feature.mypage.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun MyPageNavigatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Text(
        text = text,
        style = SoptTheme.typography.body14M,
        color = SoptTheme.colors.onSurface100,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = SoptTheme.colors.onSurface100,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
private fun MyPageNavigatorButtonPreview() {
    SoptTheme {
        MyPageNavigatorButton(
            text = "메하하",
            onClick = {}
        )
    }
}