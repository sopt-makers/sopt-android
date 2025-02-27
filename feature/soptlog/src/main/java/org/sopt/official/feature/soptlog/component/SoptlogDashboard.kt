/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
