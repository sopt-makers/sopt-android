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

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.throttledNoRippleClickable
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.components.avatar.MdsAvatar
import org.sopt.official.mds.components.avatar.MdsAvatarFallbackType
import org.sopt.official.mds.theme.SoptTheme

private val AvatarStrokeWidth = 2.dp

/**
 * 익명이 섞일 수 있는 리스트(알림 / 친구 목록)용 아바타
 *
 * 익명 여부에 따라 [PokeAnonymousAvatar] / [PokeProfileAvatar] 로 분기한다.
 * 추천 친구 카드처럼 익명이 올 수 없는 곳은 [PokeProfileAvatar] 를 직접 사용한다.
 *
 * @param user            표시할 유저
 * @param size            아바타 크기
 * @param onProfileClick  프로필 탭 시 userId 전달 (익명이면 탭 불가)
 */
@Composable
internal fun PokeAvatar(
    user: PokeUserUiState,
    size: Dp,
    onProfileClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strokeColor = when (user.relationName) {
        PokeFriendType.NEW.readableName -> SoptTheme.colors.stroke.secondary.default
        PokeFriendType.BEST_FRIEND.readableName -> SoptTheme.colors.fg.success.default
        PokeFriendType.SOULMATE.readableName -> SoptTheme.colors.stroke.brand.default
        else -> Color.Transparent
    }

    if (user.isAnonymousVisible) {
        PokeAnonymousAvatar(
            size = size,
            strokeColor = strokeColor,
            modifier = modifier,
        )
    } else {
        PokeProfileAvatar(
            imageUrl = user.profileImageUrl,
            size = size,
            strokeColor = strokeColor,
            onClick = { onProfileClick(user.userId) },
            modifier = modifier,
        )
    }
}

/**
 * 익명 유저 아바타 - 고정 이미지, 탭 불가
 */
@Composable
internal fun PokeAnonymousAvatar(
    size: Dp,
    strokeColor: Color,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.image_anonymous_profile),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(width = AvatarStrokeWidth, color = strokeColor, shape = CircleShape),
    )
}

/**
 * 일반 유저 아바타 - 프로필 이미지 로드, 탭 시 프로필 이동
 */
@Composable
internal fun PokeProfileAvatar(
    imageUrl: String?,
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    strokeColor: Color? = null,
) {
    MdsAvatar(
        imageUrl = imageUrl.orEmpty(),
        size = size,
        fallbackType = MdsAvatarFallbackType.SUBTLE,
        strokeColor = strokeColor,
        modifier = modifier.throttledNoRippleClickable(onClick = onClick),
    )
}
