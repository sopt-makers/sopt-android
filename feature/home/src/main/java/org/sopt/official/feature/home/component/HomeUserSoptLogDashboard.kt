/*
 * MIT License
 * Copyright 2024-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.components.avatar.MdsAvatar
import org.sopt.official.mds.components.tag.MdsTagType
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun HomeUserSoptLogDashboardForVisitor(
    onDashboardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeBox(
        modifier = modifier
            .clickable(onClick = onDashboardClick)
            .fillMaxWidth(),
        content = {
            Column(
                modifier = Modifier.padding(all = 16.dp),
            ) {
                Text(
                    text = "안녕하세요.\nSOPT의 열정이 되어주세요!",
                    style = SoptTheme.typography.heading4,
                    color = SoptTheme.colors.fg.neutral.bold,
                )
                Spacer(modifier = Modifier.height(height = 8.dp))
                RecentGenerationChip(
                    text = "비회원",
                    tagType = MdsTagType.DEFAULT
                )
            }
        }
    )
}

@Preview
@Composable
private fun HomeUserSoptLogDashboardForVisitorPreview() {
    SoptTheme {
        HomeUserSoptLogDashboardForVisitor(
            onDashboardClick = {}
        )
    }
}

@Composable
internal fun HomeUserSoptLogDashboardForMember(
    homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel,
    onDashboardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeBox(
        modifier = modifier.fillMaxWidth(),
        content = {
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SoptTheme.typography.heading4.toSpanStyle()
                            ) { append(homeUserSoptLogDashboardModel.emphasizedDescription) }
                            append(homeUserSoptLogDashboardModel.remainingDescription)
                        },
                        style = SoptTheme.typography.body1,
                        color = SoptTheme.colors.fg.neutral.bold
                    )

                    HomeGenerationChips(homeUserSoptLogDashboardModel = homeUserSoptLogDashboardModel)
                }

                Box(
                    modifier = Modifier
                        .clickable(onClick = onDashboardClick)
                ) {
                    MdsAvatar(
                        imageUrl = homeUserSoptLogDashboardModel.userProfile,
                        size = 54.dp,
                        modifier = Modifier.padding(end = 2.dp)
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(id = MdsIcons.writeOutlined),
                        contentDescription = null,
                        tint = SoptTheme.colors.fg.neutral.bold,
                        modifier = Modifier
                            .size(size = 20.dp)
                            .align(Alignment.BottomEnd)
                            .background(color = SoptTheme.colors.bg.neutral.default, shape = CircleShape)
                            .border(width = 3.dp, color = SoptTheme.colors.bg.layer.default, shape = CircleShape)
                            .padding(6.dp)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun HomeUserSoptLogDashboardForMemberPreview() {
    SoptTheme {
        HomeUserSoptLogDashboardForMember(
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(),
            onDashboardClick = {},
        )
    }
}
