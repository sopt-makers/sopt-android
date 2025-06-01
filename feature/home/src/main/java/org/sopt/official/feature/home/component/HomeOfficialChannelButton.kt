package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.home.R

private val officialChannelList = persistentListOf(
    Triple(R.drawable.ic_homepage_24, "홈페이지", "https://www.sopt.org/"),
    Triple(R.drawable.ic_instagram_24, "인스타", "https://www.instagram.com/sopt_official/"),
    Triple(R.drawable.ic_youtube_24, "유튜브", "https://www.youtube.com/@SOPTMEDIA")
)

@Composable
internal fun HomeOfficialChannelButton(
    navigateToWebUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .width(IntrinsicSize.Max)
    ) {
        officialChannelList.forEach { (icon, title, link) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SoptTheme.colors.onSurface800)
                    .padding(vertical = 10.dp, horizontal = 8.dp)
                    .clickable { navigateToWebUrl(link) }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null,
                    tint = SoptTheme.colors.onSurface100
                )

                Text(
                    text = title,
                    style = SoptTheme.typography.body14M,
                    color = SoptTheme.colors.onSurface100
                )
            }
        }
    }
}
