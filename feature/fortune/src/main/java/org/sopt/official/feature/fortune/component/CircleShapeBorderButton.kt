package org.sopt.official.feature.fortune.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
fun CircleShapeBorderButton(
    content: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    borderWidth: Dp = 1.dp,
    borderColor: Color = SoptTheme.colors.primary,
    contentAlignment: Alignment = Alignment.Center,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .border(
                width = borderWidth,
                color = borderColor,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}

@Preview
@Composable
fun RoundedCornerButtonPreview() {
    SoptTheme {
        CircleShapeBorderButton(
            content = {
                Text(
                    text = "홈으로 돌아가기",
                    style = SoptTheme.typography.label18SB,
                    color = SoptTheme.colors.onBackground,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }
        ) { }
    }
}