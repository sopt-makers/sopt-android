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
package org.sopt.official.feature.poke.v2.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import org.sopt.official.feature.poke.v2.component.PokeMessageBottomSheet
import org.sopt.official.feature.poke.v2.component.PokeMessageRow
import org.sopt.official.feature.poke.v2.component.PokeRelationChangeOverlay
import org.sopt.official.feature.poke.v2.component.PokeSnackBarType
import org.sopt.official.feature.poke.v2.component.PokeSnackBarVisuals
import org.sopt.official.feature.poke.v2.main.model.PokeMessageRowUiState
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.model.PokeRelationChangeState
import org.sopt.official.feature.poke.v2.notification.component.PokeNotificationTopAppBar
import org.sopt.official.mds.theme.SoptTheme

@Composable
fun PokeNotificationRoute(
    navigateUp: () -> Unit,
    navigateToProfile: (Int) -> Unit,
    onShowSnackbar: (PokeSnackBarVisuals) -> Unit,
    viewModel: PokeNotificationViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val notifications = viewModel.notifications.collectAsLazyPagingItems()

    var lottieState by remember { mutableStateOf<PokeRelationChangeState?>(null) }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is PokeNotificationSideEffect.ShowErrorSnackbar -> {
                        onShowSnackbar(PokeSnackBarVisuals(message = sideEffect.message, type = PokeSnackBarType.FAILURE))
                    }

                    is PokeNotificationSideEffect.ShowWarningSnackbar -> {
                        onShowSnackbar(PokeSnackBarVisuals(message = sideEffect.message, type = PokeSnackBarType.WARNING))
                    }

                    is PokeNotificationSideEffect.ShowSuccessSnackbar -> {
                        onShowSnackbar(PokeSnackBarVisuals(message = sideEffect.message, type = PokeSnackBarType.SUCCESS))
                    }

                    is PokeNotificationSideEffect.ShowLottie -> {
                        lottieState = sideEffect.state
                    }
                }
            }
    }

    PokeNotificationScreen(
        uiState = uiState,
        notifications = notifications,
        onNavigationIconClick = navigateUp,
        onProfileClick = navigateToProfile,
        onPokeClick = viewModel::onPokeButtonClick,
        onMessageClick = viewModel::onMessageClick,
        onBottomSheetDismiss = viewModel::closeBottomSheet,
        onAnonymousCheckboxClick = viewModel::toggleAnonymous,
        onRefresh = viewModel::onRefresh
    )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokeNotificationScreen(
    uiState: PokeNotificationUiState,
    notifications: LazyPagingItems<PokeMessageRowUiState>,
    onNavigationIconClick: () -> Unit,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    onMessageClick: (PokeMessageUiState) -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onAnonymousCheckboxClick: () -> Unit,
    onRefresh: () -> Unit
) {
    val isRefreshing = notifications.loadState.refresh is LoadState.Loading

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            onRefresh()
            notifications.refresh()
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PokeNotificationTopAppBar(onNavigationIconClick = onNavigationIconClick)

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "누가 나를 찔렀어요",
                    style = SoptTheme.typography.title4,
                    color = SoptTheme.colors.fg.neutral.bold
                )

                Text(
                    text = "나도 찔러서 답장을 해보세요",
                    style = SoptTheme.typography.label4,
                    color = SoptTheme.colors.fg.neutral.subtle
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = notifications.itemCount,
                    key = { index -> notifications[index]?.user?.userId ?: index }
                ) { index ->
                    val notification = notifications[index]

                    notification?.let {
                        PokeMessageRow(
                            item = notification,
                            onProfileClick = onProfileClick,
                            onPokeClick = onPokeClick
                        )
                    }
                }
            }
        }
    }

    // TODO: 이거 스크린 안으로 넣을지 라우트에 둘지 고민
    if (uiState.bottomSheetVisibility) {
        PokeMessageBottomSheet(
            title = uiState.header,
            messages = uiState.pokeMessages,
            isAnonymous = uiState.isAnonymous,
            isAnonymousCheckboxLocked = false,
            onMessageClick = onMessageClick,
            onDismissRequest = onBottomSheetDismiss,
            onAnonymousCheckboxClick = onAnonymousCheckboxClick
        )
    }
}
