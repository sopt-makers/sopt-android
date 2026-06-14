package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.common.util.throttledNoRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.R
import org.sopt.official.feature.poke.v2.main.model.FriendListUiState
import org.sopt.official.feature.poke.v2.main.model.PokeFriendListSections
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.main.model.emptyPokeFriendListSections

@Composable
internal fun PokeFriendListBlock(
    sections: PokeFriendListSections,
    onFriendListClick: () -> Unit, // TODO: 로직에 맞게 수정
    onProfileClick: (Int) -> Unit, // TODO: 로직에 맞게 수정
    onPokeClick: (PokeUserUiState) -> Unit, // TODO: 로직에 맞게 수정
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        sections.forEachIndexed { index, (type, sectionState) ->
            PokeFriendListSection(
                type = type,
                state = sectionState,
                onFriendListClick = { onFriendListClick() },
                onProfileClick = onProfileClick,
                onPokeClick = onPokeClick
            )
            if (index < sections.lastIndex) {
                HorizontalDivider(
                    thickness = 8.dp,
                    color = SoptTheme.colors.onSurface800
                )
            }
        }
    }
}

@Composable
private fun PokeFriendListSection(
    type: PokeFriendType,
    state: FriendListUiState,
    onFriendListClick: () -> Unit,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        PokeFriendListHeader(
            type = type,
            state = state,
            onFriendListClick = onFriendListClick
        )

        if (state.isEmpty) {
            PokeFriendListEmpty(
                modifier = Modifier.padding(top = 10.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                // 최대 2개
                state.items.take(n = 2).fastForEach { user ->
                    PokeUserItem(
                        user = user,
                        onProfileClick = onProfileClick,
                        onPokeClick = onPokeClick
                    )
                }
            }
        }
    }
}

@Composable
private fun PokeFriendListHeader(
    type: PokeFriendType,
    state: FriendListUiState,
    onFriendListClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = type.title,
            style = SoptTheme.typography.heading18B,
            color = SoptTheme.colors.onSurface30,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = type.description,
            style = SoptTheme.typography.body13M,
            color = SoptTheme.colors.onSurface300,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "${state.friendCount}명",
            style = SoptTheme.typography.body14M,
            color = SoptTheme.colors.onSurface30,
            maxLines = 1,
            modifier = Modifier.padding(end = 8.dp)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .throttledNoRippleClickable(onClick = onFriendListClick)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PokeFriendListBlockPreview(
    @PreviewParameter(PokeFriendListBlockPreviewParameterProvider::class) sections: PokeFriendListSections,
) {
    SoptTheme {
        PokeFriendListBlock(
            sections = sections,
            onFriendListClick = {},
            onProfileClick = {},
            onPokeClick = {}
        )
    }
}

private class PokeFriendListBlockPreviewParameterProvider : PreviewParameterProvider<PokeFriendListSections> {
    override val values = sequenceOf(
        persistentListOf(
            PokeFriendType.NEW to FriendListUiState(
                friendCount = 2,
                items = persistentListOf(
                    PokeUserUiState(
                        userId = 1,
                        userName = "김솝트",
                        anonymousName = "수상한 솝트인",
                        userGeneration = 36,
                        userPart = "안드로이드",
                        profileImageUrl = null,
                        pokeCount = 3,
                        relationName = PokeFriendType.NEW.readableName,
                        isPokeButtonEnabled = false
                    ),
                    PokeUserUiState(
                        userId = 2,
                        userName = "김솝트",
                        anonymousName = "수상한 솝트인",
                        userGeneration = 36,
                        userPart = "안드로이드",
                        profileImageUrl = null,
                        isAnonymous = true,
                        pokeCount = 4,
                        relationName = PokeFriendType.NEW.readableName,
                    )
                )
            ),
            PokeFriendType.BEST_FRIEND to FriendListUiState(
                friendCount = 1,
                items = persistentListOf(
                    PokeUserUiState(
                        userId = 2,
                        userName = "박메이커",
                        anonymousName = "익명의 메이커",
                        userGeneration = 36,
                        userPart = "디자인",
                        profileImageUrl = null,
                        pokeCount = 7,
                        relationName = PokeFriendType.BEST_FRIEND.readableName,
                        isAnonymous = true,
                        isPokeButtonEnabled = false
                    )
                )
            ),
            PokeFriendType.SOULMATE to FriendListUiState(
                friendCount = 0,
                items = persistentListOf()
            )
        ),
        emptyPokeFriendListSections()
    )
}