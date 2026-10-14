/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.theme.SoptTheme

private val AvatarSize = 48.dp

/**
 * 아바타 이름 찌르기 버튼으로 구성된 유저 한 줄 레이아웃
 *
 * @param user             표시할 유저
 * @param onProfileClick   프로필 탭 시 userId 전달
 * @param onPokeClick      콕 찌르기 버튼 클릭
 * @param avatarAlignment  아바타 세로 정렬
 * @param nameStyle        이름 텍스트 스타일
 * @param trailingContent    찌르기 버튼 왼쪽에 놓이는 콘텐츠 (예: 콕 수)
 * @param supportingContent  이름 아래에 놓이는 콘텐츠 (예: 메시지, 관계 태그)
 */
@Composable
internal fun PokeUserRowLayout(
    user: PokeUserUiState,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier,
    avatarAlignment: Alignment.Vertical = Alignment.CenterVertically,
    nameStyle: TextStyle = SoptTheme.typography.title5,
    trailingContent: @Composable RowScope.() -> Unit = {},
    supportingContent: @Composable ColumnScope.() -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SoptTheme.spacing.s12),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SoptTheme.spacing.s20, vertical = SoptTheme.spacing.s10),
    ) {
        PokeAvatar(
            user = user,
            size = AvatarSize,
            onProfileClick = onProfileClick,
            modifier = Modifier.align(avatarAlignment),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(SoptTheme.spacing.s8),
            modifier = Modifier.weight(1f),
        ) {
            PokeUserNameInfo(user = user, nameStyle = nameStyle)
            supportingContent()
        }

        trailingContent()

        PokeButton(
            enabled = user.isPokeButtonEnabled,
            onClick = { onPokeClick(user) },
        )
    }
}

/**
 * 이름 + 기수 + 파트
 *
 * @param user       표시할 유저
 * @param nameStyle  이름 텍스트 스타일
 */
@Composable
private fun PokeUserNameInfo(
    user: PokeUserUiState,
    nameStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SoptTheme.spacing.s8),
        modifier = modifier,
    ) {
        Text(
            text = user.displayName,
            style = nameStyle,
            color = SoptTheme.colors.fg.neutral.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (!user.isAnonymousVisible) {
            Text(
                text = user.generationPartText,
                style = SoptTheme.typography.label4,
                color = SoptTheme.colors.fg.neutral.subtle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
