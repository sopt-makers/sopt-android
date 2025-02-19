package org.sopt.official.feature.soptlog.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.UrlImage
import org.sopt.official.feature.soptlog.R

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
        if (profileImageUrl.isEmpty()) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_empty_profile),
                contentDescription = "프로필 사진",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        } else {
            UrlImage(
                url = profileImageUrl,
                contentDescription = "프로필 사진",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        }

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

@Preview
@Composable
fun PreviewSoptlogProfile() {
    SoptTheme {
        SoptlogProfile(
            "https://avatars.githubusercontent.com/u/48426991?v=4",
            "차은우",
            "안드로이드"
        )
    }
}
