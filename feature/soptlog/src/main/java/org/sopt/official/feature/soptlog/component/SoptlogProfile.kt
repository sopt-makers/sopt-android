package org.sopt.official.feature.soptlog.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.test.FakeImage
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.UrlImage

@Composable
fun SoptlogProfile(
    profileImageUrl: String,
    name: String,
    part: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        UrlImage(
            url = profileImageUrl,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentDescription = "프로필 사진"
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = name,
                style = SoptTheme.typography.heading20B,
                color = SoptTheme.colors.surface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = part,
                style = SoptTheme.typography.body13M,
                color = SoptTheme.colors.onSurface100
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
fun PreviewSoptlogProfile() {
    SoptTheme {
        val previewHandler = AsyncImagePreviewHandler {
            FakeImage(color = 0xFFE0E0E0.toInt())
        }

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            SoptlogProfile(
                "https://avatars.githubusercontent.com/u/48426991?v=4",
                "차은우",
                "안드로이드"
            )
        }
    }
}
