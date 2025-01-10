package org.sopt.official.feature.soptlog.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.UrlImage

@Composable
fun SoptlogDashBoard(
    dashBoardItems: ImmutableList<DashBoardItem>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SoptTheme.colors.onSurface800)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        dashBoardItems.forEach { item ->
            key(
                item
            ) {
                SoptlogDashBoardItem(
                    title = item.title,
                    iconUrl = item.iconUrl,
                    content = item.content,
                )
            }
        }
    }
}

data class DashBoardItem(
    val title: String,
    val iconUrl: String,
    val content: String,
)

@Composable
private fun SoptlogDashBoardItem(
    title: String,
    iconUrl: String,
    content: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = SoptTheme.typography.body14M,
            color = SoptTheme.colors.onSurface200
        )

        UrlImage(
            url = iconUrl,
            modifier = Modifier
                .padding(top = 6.dp, bottom = 4.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(SoptTheme.colors.surface),
            contentDescription = "프로필 사진"
        )

        Text(
            text = content,
            style = SoptTheme.typography.heading16B,
            color = SoptTheme.colors.surface
        )
    }
}

@Preview
@Composable
fun SoptlogDashBoardItemPreview() {
    SoptTheme {
        SoptlogDashBoardItem(
            title = "솝트레벨",
            iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo.png",
            content = "Lv.6",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SoptlogDashBoardPreview() {
    SoptTheme {
        SoptlogDashBoard(
            dashBoardItems = persistentListOf(
                DashBoardItem(
                    title = "솝트레벨",
                    iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo.png",
                    content = "Lv.6",
                ),
                DashBoardItem(
                    title = "콕찌르기",
                    iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo2.png",
                    content = "208회",
                ),
                DashBoardItem(
                    title = "솝트와",
                    iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo2.png",
                    content = "33개월",
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
