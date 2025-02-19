package org.sopt.official.feature.soptlog.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.soptlog.R

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
                    icon = item.icon,
                    content = item.content,
                )
            }
        }
    }
}

data class DashBoardItem(
    val title: String,
    @DrawableRes val icon: Int,
    val content: String,
)

@Composable
private fun SoptlogDashBoardItem(
    title: String,
    @DrawableRes icon: Int,
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

        Image(
            imageVector = ImageVector.vectorResource(icon),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SoptTheme.colors.onSurface700)
                .padding(vertical = 7.dp, horizontal = 6.dp),
            contentDescription = null
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
            icon = R.drawable.ic_sopt_level,
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
                    icon = R.drawable.ic_sopt_level,
                    content = "Lv.6",
                ),
                DashBoardItem(
                    title = "콕찌르기",
                    icon = R.drawable.ic_poke_hand,
                    content = "208회",
                ),
                DashBoardItem(
                    title = "솝트와",
                    icon = R.drawable.ic_calender,
                    content = "33개월",
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
