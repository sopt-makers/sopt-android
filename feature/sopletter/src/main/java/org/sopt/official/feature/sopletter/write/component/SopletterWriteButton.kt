package org.sopt.official.feature.sopletter.write.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.throttledNoRippleClickable
import org.sopt.official.designsystem.SoptTheme

@Composable
fun SopletterWriteButton(
    isEnabled: Boolean,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isEnabled) SoptTheme.colors.onSurface600 else SoptTheme.colors.onSurface100

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .then(
                if (isEnabled) {
                    Modifier.throttledNoRippleClickable(onClick = onButtonClick)
                } else {
                    Modifier
                }
            )
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "작성완료",
            style = SoptTheme.typography.title16SB,
            color = if (isEnabled) SoptTheme.colors.primary else SoptTheme.colors.onSurface300
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SopletterWriteButtonPreview() {
    SoptTheme {
        SopletterWriteButton(
            isEnabled = true,
            onButtonClick = {}
        )
    }
}