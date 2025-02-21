package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.White

@Composable
internal fun HomeBox(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = CenterStart,
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier.background(
            color = colors.onSurface900,
            shape = RoundedCornerShape(size = 8.dp),
        ),
    ) {
        content()
    }
}

@Preview
@Composable
private fun HomeBoxPreview() {
    SoptTheme {
        HomeBox(
            content = {
                Text(
                    text = "123",
                    color = White
                )
            }
        )
    }
}
