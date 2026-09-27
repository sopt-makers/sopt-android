package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.main.model.PokeMessageRowUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.components.tag.MdsTag
import org.sopt.official.mds.components.tag.MdsTagEmphasis
import org.sopt.official.mds.components.tag.MdsTagShape
import org.sopt.official.mds.components.tag.MdsTagSize
import org.sopt.official.mds.components.tag.MdsTagType
import org.sopt.official.mds.theme.SoptTheme

/**
 * 받은 콕 메시지 행. 이름 기수 파트 메시지 관계 태그
 *
 * @param item            표시할 항목
 * @param onProfileClick  프로필 탭 시 userId 전달
 * @param onPokeClick     찌르기 버튼 탭 시 유저 전달
 * @param isHighlighted   강조 배경 표시 여부
 */
@Composable
internal fun PokeMessageRow(
    item: PokeMessageRowUiState,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false,
) {
    val backgroundModifier = if (isHighlighted) {
        Modifier.background(SoptTheme.colors.bg.neutral.ghost)
    } else {
        Modifier
    }

    PokeUserRowLayout(
        user = item.user,
        onProfileClick = onProfileClick,
        onPokeClick = onPokeClick,
        avatarAlignment = Alignment.Top,
        nameStyle = SoptTheme.typography.label3,
        modifier = modifier.then(backgroundModifier),
    ) {
        PokeUserMessage(message = item.message)
        if (item.relationTagText.isNotBlank()) {
            PokeRelationTag(text = item.relationTagText)
        }
    }
}

/**
 * 받은 콕 메시지
 *
 * @param message  메시지 내용
 */
@Composable
private fun PokeUserMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        style = SoptTheme.typography.body2,
        color = SoptTheme.colors.fg.neutral.bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

/**
 * 관계 태그
 *
 * @param text  태그 문구
 */
@Composable
private fun PokeRelationTag(
    text: String,
    modifier: Modifier = Modifier,
) {
    MdsTag(
        text = text,
        type = MdsTagType.DEFAULT,
        emphasis = MdsTagEmphasis.SUBTLE,
        size = MdsTagSize.SMALL,
        shape = MdsTagShape.RECT,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1012)
@Composable
private fun PokeMessageRowPreview() {
    SoptTheme {
        Column {
            // alarm_list
            PokeMessageRow(
                item = PokeMessageRowUiState(
                    user = PokeUserUiState(
                        userId = 1,
                        userName = "차은우",
                        userGeneration = 29,
                        userPart = "안드로이드",
                        profileImageUrl = null,
                        relationName = PokeFriendType.NEW.readableName,
                        isPokeButtonEnabled = false,
                        isFirstMeet = true,
                    ),
                    message = "친해지고 싶어요! 두줄이되면 점점점점점점점",
                    relationTagText = "재갈송현 외 1명과 친구",
                ),
                onProfileClick = {},
                onPokeClick = {},
                isHighlighted = true,
            )
            // notiList_main
            PokeMessageRow(
                item = PokeMessageRowUiState(
                    user = PokeUserUiState(
                        userId = 2,
                        userName = "김솝트",
                        anonymousName = "익명의 사자",
                        userGeneration = 29,
                        userPart = "안드로이드",
                        profileImageUrl = null,
                        pokeCount = 5,
                        relationName = PokeFriendType.SOULMATE.readableName,
                        isAnonymous = true,
                    ),
                    message = "친해지고 싶어요! 두줄이되면 점점점점점점점",
                    relationTagText = "",
                ),
                onProfileClick = {},
                onPokeClick = {},
            )
            // notiList_main
            PokeMessageRow(
                item = PokeMessageRowUiState(
                    user = PokeUserUiState(
                        userId = 3,
                        userName = "이승호가나",
                        userGeneration = 29,
                        userPart = "안드로이드",
                        profileImageUrl = null,
                        pokeCount = 5,
                        relationName = PokeFriendType.BEST_FRIEND.readableName,
                    ),
                    message = "친해지고 싶어요! 두줄이되면 점점점점점점점",
                    relationTagText = "친한친구 5콕",
                ),
                onProfileClick = {},
                onPokeClick = {},
            )
        }
    }
}
