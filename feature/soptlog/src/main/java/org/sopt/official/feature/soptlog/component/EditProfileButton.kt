package org.sopt.official.feature.soptlog.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme


@Composable
fun EditProfileButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = SoptTheme.colors.onSurface100,
                shape = CircleShape
            )
            .padding(vertical = 9.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "프로필 수정",
            style = SoptTheme.typography.label14SB,
            color = SoptTheme.colors.onSurface100
        )
    }
}

@Preview
@Composable
fun EditProfileButtonPreview() {
    SoptTheme {
        EditProfileButton(
            onClick = {}
        )
    }
}
