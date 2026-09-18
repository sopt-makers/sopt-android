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
            strokeColor = user.relationStrokeColor,
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
