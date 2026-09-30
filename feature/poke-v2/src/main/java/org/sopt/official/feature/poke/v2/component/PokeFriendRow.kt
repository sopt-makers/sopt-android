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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.theme.SoptTheme

/**
 * 친구 목록 행. 이름 기수 파트 누적 콕 수
 *
 * @param user            표시할 유저
 * @param onProfileClick  프로필 탭 시 userId 전달
 * @param onPokeClick     찌르기 버튼 탭 시 유저 전달
 * @param showDivider     하단 구분선 표시 여부
 */
@Composable
internal fun PokeFriendRow(
    user: PokeUserUiState,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = false,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        PokeUserRowLayout(
            user = user,
            onProfileClick = onProfileClick,
            onPokeClick = onPokeClick,
            trailingContent = { PokeCountText(count = user.pokeCount) },
        )

        if (showDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = SoptTheme.colors.stroke.neutral.subtle,
            )
        }
    }
}

/**
 * 누적 찌르기 횟수
 *
 * @param count  콕 수
 */
@Composable
private fun PokeCountText(
    count: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "${count}콕",
        style = SoptTheme.typography.label2,
        color = SoptTheme.colors.fg.neutral.bold,
        maxLines = 1,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1012)
@Composable
private fun PokeFriendRowPreview() {
    SoptTheme {
        Column {
            PokeFriendRow(
                user = PokeUserUiState(
                    userId = 3,
                    userName = "이승호가나",
                    userGeneration = 29,
                    userPart = "안드로이드",
                    profileImageUrl = null,
                    pokeCount = 99,
                    relationName = PokeFriendType.NEW.readableName,
                ),
                onProfileClick = {},
                onPokeClick = {},
            )

            PokeFriendRow(
                user = PokeUserUiState(
                    userId = 4,
                    userName = "황보혜정",
                    userGeneration = 33,
                    userPart = "안드로이드",
                    profileImageUrl = null,
                    pokeCount = 2,
                    relationName = PokeFriendType.BEST_FRIEND.readableName,
                ),
                onProfileClick = {},
                onPokeClick = {},
                showDivider = true,
            )
        }
    }
}
