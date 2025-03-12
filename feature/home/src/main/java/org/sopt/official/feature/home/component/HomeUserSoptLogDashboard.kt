/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.Black40
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.designsystem.SuitBold
import org.sopt.official.feature.home.R.drawable.ic_soptlog
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel

@Composable
internal fun HomeUserSoptLogDashboardForVisitor(
    onDashboardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDashboardClick() },
        content = {
            Column(
                modifier = Modifier.padding(all = 16.dp),
            ) {
                Text(
                    text = "안녕하세요.\nSOPT의 열정이 되어주세요!",
                    style = typography.body18M,
                    color = colors.onBackground,
                )
                Spacer(modifier = Modifier.height(height = 12.dp))
                RecentGenerationChip(
                    chipColor = Black40,
                    textColor = colors.onBackground,
                    text = "비회원"
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
            onDashboardClick = {},
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
                modifier = Modifier
                    .padding(all = 16.dp)
                    .clickable { onDashboardClick() },
            ) {
                Column {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = ParagraphStyle(lineHeight = 28.sp)) {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = SuitBold,
                                        fontSize = 18.sp,
                                        letterSpacing = (-0.02).em
                                    )
                                ) { append(homeUserSoptLogDashboardModel.emphasizedDescription) }
                                append(homeUserSoptLogDashboardModel.remainingDescription)
                            }
                        },
                        style = typography.body18M,
                        color = colors.onBackground,
                    )
                    Spacer(modifier = Modifier.height(height = 12.dp))
                    HomeGenerationChips(homeUserSoptLogDashboardModel = homeUserSoptLogDashboardModel)
                }
                Spacer(modifier = Modifier.weight(weight = 1f))
                Icon(
                    imageVector = ImageVector.vectorResource(ic_soptlog),
                    contentDescription = null,
                    tint = Unspecified,
                )
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
