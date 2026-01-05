package org.sopt.official.feature.appjamtamp.missiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.R

@Composable
internal fun ProfileTag(
    name: String,
    profileImage: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(size = 22.dp)
                .clip(CircleShape)
                .background(color = SoptTheme.colors.onSurface700),
            contentAlignment = Alignment.Center
        ) {
            if (profileImage.isNotBlank()) {
                AsyncImage(
                    model = profileImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_user_profile)
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_user_profile),
                    contentDescription = null,
                    tint = SoptTheme.colors.onSurface500,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = name,
            style = SoptTheme.typography.title14SB,
            color = SoptTheme.colors.onSurface10
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_32),
            contentDescription = null,
            tint = SoptTheme.colors.onSurface10,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Preview
@Composable
private fun ProfileTagPreview() {
    SoptTheme {
        ProfileTag(
            name = "스푸니박효빈",
            profileImage = ""
        )
    }
}
