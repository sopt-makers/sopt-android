package org.sopt.official.feature.poke.v2.friend.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.sopt.official.common.util.throttledNoRippleClickable
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.component.PokeFriendListEmpty
import org.sopt.official.feature.poke.v2.component.PokeFriendRow
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.theme.SoptTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokeFriendListBottomSheet(
    type: PokeFriendType,
    friendCount: Int,
    friends: ImmutableList<PokeUserUiState>,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SoptTheme.colors.bg.neutral.ghost,
        shape = RoundedCornerShape(
            topStart = SoptTheme.radius.r20,
            topEnd = SoptTheme.radius.r20,
        ),
        dragHandle = null,
        modifier = modifier.statusBarsPadding(),
    ) {
        PokeFriendListBottomSheetContent(
            type = type,
            friendCount = friendCount,
            friends = friends,
            onCloseClick = {
                scope.launch {
                    sheetState.hide()
                    onDismissRequest()
                }
            },
            onProfileClick = onProfileClick,
            onPokeClick = onPokeClick,
        )
    }
}

@Composable
internal fun PokeFriendListBottomSheetContent(
    type: PokeFriendType,
    friendCount: Int,
    friends: ImmutableList<PokeUserUiState>,
    onCloseClick: () -> Unit,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = SoptTheme.spacing.s24,
            ),
    ) {
        PokeFriendListHeader(
            type = type,
            friendCount = friendCount,
            onCloseClick = onCloseClick,
        )

        if (friends.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                PokeFriendListEmpty()
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
            ) {
                items(
                    items = friends,
                    key = { it.userId },
                ) { friend ->
                    PokeFriendRow(
                        user = friend,
                        onProfileClick = onProfileClick,
                        onPokeClick = onPokeClick,
                        showDivider = true,
                    )
                }
            }
        }
    }
}

@Composable
private fun PokeFriendListHeader(
    type: PokeFriendType,
    friendCount: Int,
    onCloseClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = SoptTheme.spacing.s20,
                vertical = SoptTheme.spacing.s10
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = type.title,
            style = SoptTheme.typography.title4,
            color = SoptTheme.colors.fg.neutral.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = type.description,
            style = SoptTheme.typography.label4,
            color = SoptTheme.colors.fg.neutral.subtle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = SoptTheme.spacing.s8)
        )
        Text(
            text = "${friendCount}명",
            style = SoptTheme.typography.label3,
            color = SoptTheme.colors.fg.neutral.bold,
            maxLines = 1,
            modifier = Modifier.padding(end = SoptTheme.spacing.s8)
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = MdsIcons.xCloseOutlined),
            contentDescription = null,
            tint = SoptTheme.colors.fg.neutral.bold,
            modifier = Modifier
                .size(24.dp)
                .throttledNoRippleClickable(onClick = onCloseClick)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 640)
@Composable
private fun PokeFriendListBottomSheetPreview() {
    val type = PokeFriendType.NEW
    val friends = persistentListOf(
        PokeUserUiState(
            userId = 1,
            userName = "김솝트",
            anonymousName = "수상한 솝트인",
            userGeneration = 36,
            userPart = "안드로이드",
            profileImageUrl = null,
            pokeCount = 3,
            relationName = type.readableName,
        ),
        PokeUserUiState(
            userId = 2,
            userName = "박메이커",
            anonymousName = "익명의 메이커",
            userGeneration = 37,
            userPart = "디자인",
            profileImageUrl = null,
            pokeCount = 4,
            relationName = type.readableName,
            isAnonymous = true,
        ),
        PokeUserUiState(
            userId = 3,
            userName = "이서버",
            userGeneration = 35,
            userPart = "서버",
            profileImageUrl = null,
            pokeCount = 2,
            relationName = type.readableName,
            isPokeButtonEnabled = false,
        ),
        PokeUserUiState(
            userId = 4,
            userName = "최기획",
            userGeneration = 34,
            userPart = "기획",
            profileImageUrl = null,
            pokeCount = 3,
            relationName = type.readableName,
        ),
    )

    SoptTheme {
        Surface(
            color = SoptTheme.colors.bg.neutral.ghost,
            shape = RoundedCornerShape(
                topStart = SoptTheme.radius.r20,
                topEnd = SoptTheme.radius.r20,
            ),
        ) {
            PokeFriendListBottomSheetContent(
                type = type,
                friendCount = friends.size,
                friends = friends,
                onCloseClick = {},
                onProfileClick = {},
                onPokeClick = {},
            )
        }
    }
}

