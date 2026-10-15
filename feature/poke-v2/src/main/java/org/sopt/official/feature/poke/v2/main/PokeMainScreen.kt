package org.sopt.official.feature.poke.v2.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import org.sopt.official.feature.poke.v2.component.PokeFriendRow
import org.sopt.official.feature.poke.v2.component.PokeMessageBottomSheet
import org.sopt.official.feature.poke.v2.component.PokeMessageRow
import org.sopt.official.feature.poke.v2.component.PokeProfileCard
import org.sopt.official.feature.poke.v2.component.PokeRelationChangeOverlay
import org.sopt.official.feature.poke.v2.component.PokeSnackBarType
import org.sopt.official.feature.poke.v2.component.PokeSnackBarVisuals
import org.sopt.official.feature.poke.v2.main.component.PokeMainFriendSection
import org.sopt.official.feature.poke.v2.main.component.PokeMainSection
import org.sopt.official.feature.poke.v2.main.model.PokeMainSideEffect
import org.sopt.official.feature.poke.v2.main.model.PokeMainUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.model.PokeRelationChangeState
import org.sopt.official.mds.theme.SoptTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeMainRoute(
    navigateToPokeNotification: () -> Unit,
    navigateToPokeFriend: () -> Unit,
    navigateToProfile: (Int) -> Unit,
    onShowSnackbar: (PokeSnackBarVisuals) -> Unit,
    paddingValues: PaddingValues,
    viewModel: PokeMainViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var lottieState by remember { mutableStateOf<PokeRelationChangeState?>(null) }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is PokeMainSideEffect.ShowSuccess -> {
                        onShowSnackbar(PokeSnackBarVisuals(message = sideEffect.message, type = PokeSnackBarType.SUCCESS))
                    }

                    is PokeMainSideEffect.ShowWarning -> {
                        onShowSnackbar(PokeSnackBarVisuals(message = sideEffect.message, type = PokeSnackBarType.WARNING))
                    }

                    is PokeMainSideEffect.ShowError -> {
                        onShowSnackbar(PokeSnackBarVisuals(message = sideEffect.message, type = PokeSnackBarType.FAILURE))
                    }

                    is PokeMainSideEffect.PokeCompleted -> {
                        lottieState = sideEffect.result
                    }
                }
            }
    }

    PokeMainScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onRefresh = viewModel::refresh,
        onNotificationClick = navigateToPokeNotification,
        onFriendClick = navigateToPokeFriend,
        onPokeClick = viewModel::onPokeButtonClick,
        onProfileClick = navigateToProfile
    )

    if (uiState.bottomSheetVisibility) {
        PokeMessageBottomSheet(
            title = uiState.header,
            messages = uiState.pokeMessages,
            isAnonymous = uiState.isAnonymous,
            isAnonymousCheckboxLocked = false,
            onMessageClick = viewModel::onMessageClick,
            onDismissRequest = viewModel::closeBottomSheet,
            onAnonymousCheckboxClick = viewModel::toggleAnonymous
        )
    }

    val currentLottie = lottieState
    currentLottie?.let {
        when (currentLottie) {
            is PokeRelationChangeState.Soulmate -> {
                PokeRelationChangeOverlay(
                    relationChange = currentLottie,
                    onRelationChangeLottieEnd = { lottieState = PokeRelationChangeState.SoulmateRevealed(currentLottie.user) },
                    onSoulmateRevealEnd = { lottieState = null },
                )
            }

            else -> {
                PokeRelationChangeOverlay(
                    relationChange = currentLottie,
                    onRelationChangeLottieEnd = { lottieState = null },
                    onSoulmateRevealEnd = { }
                )
            }
        }
    }
}

@Composable
private fun PokeMainScreen(
    uiState: PokeMainUiState,
    paddingValues: PaddingValues,
    onRefresh: () -> Unit,
    onNotificationClick: () -> Unit,
    onFriendClick: () -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    onProfileClick: (Int) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "콕 찌르기",
                style = SoptTheme.typography.title4,
                color = SoptTheme.colors.fg.neutral.bold
            )

            uiState.pokeMe?.let {
                PokeMainSection(
                    title = "누가 나를 찔렀어요",
                    onTitleClick = onNotificationClick
                ) {
                    PokeMessageRow(
                        item = uiState.pokeMe,
                        onProfileClick = onProfileClick,
                        onPokeClick = onPokeClick
                    )
                }
            }

            uiState.friend?.let {
                PokeMainSection(
                    title = "내 친구를 찔러보세요",
                    onTitleClick = onFriendClick
                ) {
                    PokeFriendRow(
                        user = uiState.friend,
                        onProfileClick = onProfileClick,
                        onPokeClick = onPokeClick,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "나와 공통점이 있는 친구들을 찔러보세요",
                style = SoptTheme.typography.title4,
                color = SoptTheme.colors.fg.neutral.bold
            )

            uiState.recommendations.forEach { recommendation ->
                PokeMainFriendSection(
                    title = recommendation.title
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PokeProfileCard(
                            user = recommendation.users[0],
                            onProfileClick = onProfileClick,
                            onPokeClick = onPokeClick
                        )

                        PokeProfileCard(
                            user = recommendation.users[0],
                            onProfileClick = onProfileClick,
                            onPokeClick = onPokeClick
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "화면을 밑으로 당기면\n다른 친구를 볼 수 있어요",
                style = SoptTheme.typography.label3,
                color = SoptTheme.colors.fg.neutral.subtle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
