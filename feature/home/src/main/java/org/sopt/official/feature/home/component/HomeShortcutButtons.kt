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
package org.sopt.official.feature.home.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SuitMedium
import org.sopt.official.feature.home.R.drawable.ic_file_text_filled
import org.sopt.official.feature.home.R.drawable.ic_folder
import org.sopt.official.feature.home.R.drawable.ic_homepage
import org.sopt.official.feature.home.R.drawable.ic_instagram_40
import org.sopt.official.feature.home.R.drawable.ic_member
import org.sopt.official.feature.home.R.drawable.ic_moim
import org.sopt.official.feature.home.R.drawable.is_playground

@Composable
fun HomeShortcutButtonsForMember(
    onPlaygroundClick: () -> Unit,
    onStudyClick: () -> Unit,
    onMemberClick: () -> Unit,
    onProjectClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        HomeShortcutButton(
            icon = is_playground,
            text = "Playground",
            onClick = onPlaygroundClick,
        )
        HomeShortcutButton(
            icon = ic_moim,
            text = "모임/스터디",
            onClick = onStudyClick,
        )
        HomeShortcutButton(
            icon = ic_member,
            text = "멤버",
            onClick = onMemberClick,
        )
        HomeShortcutButton(
            icon = ic_file_text_filled,
            text = "프로젝트",
            onClick = onProjectClick,
        )
    }
}

@Preview
@Composable
private fun HomeShortcutButtonsForMemberPreview() {
    SoptTheme {
        HomeShortcutButtonsForMember(
            onPlaygroundClick = { },
            onStudyClick = { },
            onMemberClick = { },
            onProjectClick = { },
        )
    }
}

@Composable
fun HomeShortcutButtonsForVisitor(
    onHomePageClick: () -> Unit,
    onPlaygroundClick: () -> Unit,
    onProjectClick: () -> Unit,
    onInstagramClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        HomeShortcutButton(
            icon = ic_homepage,
            text = "홈페이지",
            onClick = onHomePageClick,
        )
        HomeShortcutButton(
            icon = is_playground,
            text = "활동후기",
            onClick = onPlaygroundClick,
        )
        HomeShortcutButton(
            icon = ic_folder,
            text = "프로젝트",
            onClick = onProjectClick,
        )
        HomeShortcutButton(
            icon = ic_instagram_40,
            text = "인스타그램",
            onClick = onInstagramClick,
        )
    }
}

@Preview
@Composable
private fun HomeShortcutButtonsForVisitorPreview() {
    SoptTheme {
        HomeShortcutButtonsForVisitor(
            onHomePageClick = { },
            onPlaygroundClick = { },
            onProjectClick = { },
            onInstagramClick = { },
        )
    }
}

@Composable
private fun HomeShortcutButton(
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier.clickable { onClick() },
    ) {
        Box(modifier = Modifier.padding(horizontal = 3.dp)) {
            HomeBox(
                modifier = Modifier.size(size = 68.dp),
                contentAlignment = Center,
                content = {
                    Icon(
                        imageVector = ImageVector.vectorResource(icon),
                        contentDescription = null,
                        tint = Unspecified,
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(height = 4.dp))
        Text(
            text = text,
            style = TextStyle(
                fontFamily = SuitMedium, fontSize = 14.sp, lineHeight = 20.sp
            ),
            color = colors.onSurface200,
        )
    }
}
