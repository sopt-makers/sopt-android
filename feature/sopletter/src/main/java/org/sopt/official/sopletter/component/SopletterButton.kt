package org.sopt.official.sopletter.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme

@Composable
internal fun SopletterButton(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = SoptTheme.colors.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buttonText,
            style = SoptTheme.typography.label18SB,
            color = SoptTheme.colors.onSurface,
            modifier = Modifier
                .padding(vertical = 16.dp)
        )
    }
}

@Preview
@Composable
private fun SopletterButtonPreview() {
    SoptTheme {
        SopletterButton(
            buttonText = "솝레터 시작하기",
            onClick = {}
        )
    }
}