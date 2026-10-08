package org.sopt.official.feature.poke.v2.friend

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.common.BuildConfig
import org.sopt.official.common.util.throttledNoRippleClickable
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.component.PokeMessageBottomSheet
import org.sopt.official.feature.poke.v2.component.PokeSnackBarVisuals
import org.sopt.official.feature.poke.v2.friend.component.PokeFriendListBlock
import org.sopt.official.feature.poke.v2.friend.component.PokeFriendListBottomSheetScaffold
import org.sopt.official.feature.poke.v2.friend.component.PokeRelationChangeOverlay
import org.sopt.official.feature.poke.v2.friend.model.FriendListUiState
import org.sopt.official.feature.poke.v2.friend.model.PokeFriendListSections
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.MdsIcons
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun PokeFriendRoute(
    navigateUp: () -> Unit,
    onShowSnackbar: (PokeSnackBarVisuals) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokeFriendViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is PokeFriendSideEffect.ShowSnackbar -> onShowSnackbar(
                        PokeSnackBarVisuals(message = sideEffect.message, type = sideEffect.type),
                    )
                }
            }
    }

    PokeFriendScreen(
        uiState = uiState,
        onBackClick = navigateUp,
        onRefresh = viewModel::getFriendListSummary,
        onFriendListClick = viewModel::openFriendListSheet,
        onLoadMoreFriendList = viewModel::loadMoreFriendList,
        onFriendListSheetDismiss = viewModel::closeFriendListSheet,
        onProfileClick = { userId ->
            context.startActivity(Intent(Intent.ACTION_VIEW, PLAYGROUND_PROFILE_URL.format(userId).toUri()))
        },
        onPokeClick = viewModel::openMessageSheet,
        onMessageAnonymousClick = viewModel::toggleMessageAnonymous,
        onMessageClick = viewModel::pokeUser,
        onMessageSheetDismiss = viewModel::closeMessageSheet,
        onRelationChangeLottieEnd = viewModel::finishRelationChangeLottie,
        onSoulmateRevealEnd = viewModel::finishSoulmateReveal,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokeFriendScreen(
    uiState: PokeFriendState,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onFriendListClick: (PokeFriendType) -> Unit,
    onLoadMoreFriendList: () -> Unit,
    onFriendListSheetDismiss: () -> Unit,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    onMessageAnonymousClick: () -> Unit,
    onMessageClick: (PokeMessageUiState) -> Unit,
    onMessageSheetDismiss: () -> Unit,
    onRelationChangeLottieEnd: () -> Unit,
    onSoulmateRevealEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PokeFriendListBottomSheetScaffold(
        friendListSheet = uiState.friendListSheet,
        onProfileClick = onProfileClick,
        onPokeClick = onPokeClick,
        onLoadMore = onLoadMoreFriendList,
        onDismissRequest = onFriendListSheetDismiss,
        overlay = {
            uiState.relationChange?.let { change ->
                PokeRelationChangeOverlay(
                    relationChange = change,
                    onRelationChangeLottieEnd = onRelationChangeLottieEnd,
                    onSoulmateRevealEnd = onSoulmateRevealEnd,
                )
            }
        },
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            PokeFriendContent(
                sections = uiState.sections,
                onBackClick = onBackClick,
                onRefresh = onRefresh,
                onFriendListClick = onFriendListClick,
                onProfileClick = onProfileClick,
                onPokeClick = onPokeClick,
            )

            if (uiState.friendListSheet == null) {
                uiState.relationChange?.let { change ->
                    PokeRelationChangeOverlay(
                        relationChange = change,
                        onRelationChangeLottieEnd = onRelationChangeLottieEnd,
                        onSoulmateRevealEnd = onSoulmateRevealEnd,
                    )
                }
            }
        }
    }

    uiState.messageSheet?.let { sheet ->
        PokeMessageBottomSheet(
            title = sheet.title,
            messages = sheet.messages,
            isAnonymous = sheet.isAnonymous,
            isAnonymousCheckboxLocked = sheet.target.isAnonymousCheckboxLocked,
            onAnonymousCheckboxClick = onMessageAnonymousClick,
            onMessageClick = onMessageClick,
            onDismissRequest = onMessageSheetDismiss,
        )
    }
}

@Composable
private fun PokeFriendContent(
    sections: PokeFriendListSections,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onFriendListClick: (PokeFriendType) -> Unit,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PokeFriendTopBar(onBackClick = onBackClick)

        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = onRefresh,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            PokeFriendListBlock(
                sections = sections,
                onFriendListClick = onFriendListClick,
                onProfileClick = onProfileClick,
                onPokeClick = onPokeClick,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            )
        }
    }
}

@Composable
private fun PokeFriendTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(MdsIcons.chevronLeftOutlined),
            contentDescription = null,
            tint = SoptTheme.colors.fg.neutral.bold,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(24.dp)
                .throttledNoRippleClickable(onClick = onBackClick),
        )

        Text(
            text = "내 친구",
            color = SoptTheme.colors.fg.neutral.bold,
            style = SoptTheme.typography.title4,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

private const val PLAYGROUND_PROFILE_URL = BuildConfig.PLAYGROUND_API + "members/%d"

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PokeFriendScreenPreview() {
    val sampleUser = PokeUserUiState(
        userId = 1,
        userName = "김솝트",
        anonymousName = "수상한 솝트인",
        userGeneration = 36,
        userPart = "안드로이드",
        profileImageUrl = null,
        pokeCount = 3,
        relationName = PokeFriendType.NEW.readableName,
    )

    SoptTheme {
        PokeFriendScreen(
            uiState = PokeFriendState(
                sections = persistentListOf(
                    PokeFriendType.NEW to FriendListUiState(
                        friendCount = 2,
                        items = persistentListOf(sampleUser, sampleUser.copy(userId = 2, userName = "박메이커")),
                    ),
                    PokeFriendType.BEST_FRIEND to FriendListUiState(friendCount = 0),
                    PokeFriendType.SOULMATE to FriendListUiState(friendCount = 0),
                ),
            ),
            onBackClick = {},
            onRefresh = {},
            onFriendListClick = {},
            onLoadMoreFriendList = {},
            onFriendListSheetDismiss = {},
            onProfileClick = {},
            onPokeClick = {},
            onMessageAnonymousClick = {},
            onMessageClick = {},
            onMessageSheetDismiss = {},
            onRelationChangeLottieEnd = {},
            onSoulmateRevealEnd = {},
        )
    }
}
