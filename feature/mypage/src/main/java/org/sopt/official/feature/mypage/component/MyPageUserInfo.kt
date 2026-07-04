package org.sopt.official.feature.mypage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.designsystem.component.UrlImage
import org.sopt.official.feature.mypage.R

@Composable
internal fun MyPageUserInfo(
    name: String,
    profileImage: String,
    part: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (profileImage.isEmpty()) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_empty_profile),
                contentDescription = null,
                tint = Unspecified,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        } else {
            UrlImage(
                url = profileImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = name,
                style = SoptTheme.typography.heading20B,
                color = White
            )

            Text(
                text = part,
                style = SoptTheme.typography.body14R,
                color = SoptTheme.colors.onSurface100
            )
        }
    }
}

@Preview
@Composable
private fun MyPageUserInfoPreview() {
    SoptTheme {
        MyPageUserInfo(
            name = "손민성",
            profileImage = "",
            part = "안드로이드"
        )
    }
}