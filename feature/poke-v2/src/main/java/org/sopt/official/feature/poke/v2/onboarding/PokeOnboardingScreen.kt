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
package org.sopt.official.feature.poke.v2.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.analytics.trackViewType
import org.sopt.official.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.feature.poke.v2.main.model.PokeAnalyticsEvent
import org.sopt.official.feature.poke.v2.main.model.PokeAnalyticsPropertyKey
import org.sopt.official.feature.poke.v2.main.model.PokeClickSource
import org.sopt.official.feature.poke.v2.component.PokeMessageBottomSheet
import org.sopt.official.feature.poke.v2.component.PokeSnackBarType
import org.sopt.official.feature.poke.v2.component.PokeSnackBarVisuals
import org.sopt.official.feature.poke.v2.main.model.PokeRecommendationUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.onboarding.component.PokeOnboardingBottomSheet
import org.sopt.official.feature.poke.v2.onboarding.component.PokeOnboardingPageIndicator
import org.sopt.official.feature.poke.v2.onboarding.component.PokeOnboardingUserItem
import org.sopt.official.feature.poke.v2.onboarding.model.PokeOnboardingSideEffect
import org.sopt.official.feature.poke.v2.onboarding.model.PokeOnboardingUiState
import org.sopt.official.feature.poke.v2.main.model.toAnalyticsValue
import org.sopt.official.mds.R
import org.sopt.official.mds.theme.SoptTheme
import org.sopt.official.model.toViewType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeOnboardingRoute(
    navigateUp: () -> Unit,
    onShowSnackbar: (PokeSnackBarVisuals) -> Unit,
    navigateToProfile: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokeOnboardingViewModel = hiltViewModel(),
) {
    val tracker = LocalTracker.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val viewType = viewModel.userStatus.toViewType()

    LaunchedEffect(Unit) {
        tracker.trackViewType(PokeAnalyticsEvent.VIEW_POKE_ONBOARDING, viewType)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is PokeOnboardingSideEffect.ShowError ->
                    onShowSnackbar(
                        PokeSnackBarVisuals(
                            message = effect.message ?: "문제가 발생했습니다.",
                            type = PokeSnackBarType.FAILURE,
                        ),
                    )

                is PokeOnboardingSideEffect.PokeCompleted ->
                    onShowSnackbar(
                        PokeSnackBarVisuals(
                            message = if (effect.result.isPlainPoke) {
                                "콕 찔렀어요!"
                            } else {
                                "${effect.result.anonymousName}과 친구가 되었어요!"
                            },
                            type = PokeSnackBarType.SUCCESS,
                        ),
                    )
            }
        }
    }

    PokeOnboardingScreen(
        uiState = uiState,
        onRefresh = { viewModel.fetchOnboardingUsers(isRefresh = true) },
        onProfileClick = { userId ->
            tracker.trackViewType(
                event = PokeAnalyticsEvent.CLICK_MEMBER_PROFILE,
                viewType = viewType,
                properties = mapOf(
                    PokeAnalyticsPropertyKey.CLICK_SOURCE to PokeClickSource.ONBOARDING.value,
                    PokeAnalyticsPropertyKey.VIEW_PROFILE to userId,
                ),
            )
            navigateToProfile(userId)
        },
        onPokeClick = { user ->
            tracker.trackViewType(
                event = PokeAnalyticsEvent.CLICK_POKE_ICON,
                viewType = viewType,
                properties = mapOf(
                    PokeAnalyticsPropertyKey.CLICK_SOURCE to PokeClickSource.ONBOARDING.value,
                    PokeAnalyticsPropertyKey.VIEW_PROFILE to user.userId,
                ),
            )
            viewModel.openPokeMessageSheet(user)
        },
        modifier = modifier,
    )

    if (uiState.isError) {
        NetworkErrorDialog(onConfirm = navigateUp)
    }

    if (uiState.shouldShowGuideBottomSheet) {
        PokeOnboardingBottomSheet(onDismissRequest = viewModel::dismissGuideBottomSheet)
    }

    val pokeTarget = uiState.pokeTarget
    if (pokeTarget != null) {
        PokeMessageBottomSheet(
            messages = uiState.messages,
            isAnonymous = uiState.isAnonymous,
            isAnonymousCheckboxLocked = pokeTarget.isAnonymousCheckboxLocked,
            onAnonymousCheckboxClick = {
                val willBeAnonymous = !uiState.isAnonymous
                tracker.trackViewType(
                    event = PokeAnalyticsEvent.CLICK_POKE_ANONYMITY,
                    viewType = viewType,
                    properties = mapOf(
                        PokeAnalyticsPropertyKey.MESSAGE_TYPE to PokeMessageType.POKE_SOMEONE.toAnalyticsValue(),
                        PokeAnalyticsPropertyKey.IS_ANONYMOUS to willBeAnonymous,
                    ),
                )
                viewModel.toggleAnonymous()
                if (!willBeAnonymous) {
                    onShowSnackbar(
                        PokeSnackBarVisuals(
                            message = "익명 해제 시, 상대방이 나를 알 수 있어요.",
                            type = PokeSnackBarType.WARNING,
                        ),
                    )
                }
            },
            onMessageClick = { message ->
                tracker.trackViewType(
                    event = PokeAnalyticsEvent.CLICK_POKE_SEND_MESSAGE,
                    viewType = viewType,
                    properties = mapOf(
                        PokeAnalyticsPropertyKey.MESSAGE_TYPE to PokeMessageType.POKE_SOMEONE.toAnalyticsValue(),
                        PokeAnalyticsPropertyKey.MESSAGE_ID to message.messageId,
                        PokeAnalyticsPropertyKey.IS_ANONYMOUS to (uiState.isAnonymous && !pokeTarget.isAnonymousCheckboxLocked),
                    ),
                )
                viewModel.pokeUser(message)
            },
            onDismissRequest = viewModel::dismissPokeMessageSheet,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokeOnboardingScreen(
    uiState: PokeOnboardingUiState,
    onRefresh: () -> Unit,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { uiState.pages.size })

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SoptTheme.spacing.s20),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_x_close_outlined),
                    contentDescription = null,
                    tint = SoptTheme.colors.fg.neutral.bold,
                )

                Text(
                    text = "콕 찌르기",
                    style = SoptTheme.typography.heading4,
                    color = SoptTheme.colors.fg.neutral.bold
                )
            }

            Spacer(modifier = Modifier.height(SoptTheme.spacing.s16))

            Text(
                text = "아는 사람을 콕 찔러서 친구를 맺어보세요",
                style = SoptTheme.typography.title4,
                color = SoptTheme.colors.fg.neutral.bold,
            )

            Spacer(modifier = Modifier.height(SoptTheme.spacing.s16))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(SoptTheme.radius.r8))
                    .background(SoptTheme.colors.bg.layer.default)
                    .padding(horizontal = SoptTheme.spacing.s14),
            ) {
                HorizontalPager(
                    state = pagerState
                ) { pageIndex ->
                    val page = uiState.pages[pageIndex]

                    Column {
                        Text(
                            text = page.title,
                            style = SoptTheme.typography.title5,
                            color = SoptTheme.colors.fg.neutral.bold,
                            modifier = Modifier.padding(top = SoptTheme.spacing.s14),
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(SoptTheme.spacing.s8),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = SoptTheme.spacing.s8,
                                    bottom = SoptTheme.spacing.s14,
                                ),
                        ) {
                            page.users.chunked(2).forEach { rowUsers ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    rowUsers.forEach { user ->
                                        PokeOnboardingUserItem(
                                            user = user,
                                            onProfileClick = onProfileClick,
                                            onPokeClick = onPokeClick,
                                            modifier = Modifier.weight(1f),
                                        )
                                    }
                                    // 마지막 줄에 1개만 남으면 짝을 맞춰 폭을 절반으로 유지한다.
                                    if (rowUsers.size < 2) {
                                        Row(modifier = Modifier.weight(1f)) {}
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(SoptTheme.spacing.s14))

            if (uiState.pages.size > 1) {
                PokeOnboardingPageIndicator(
                    numberOfPages = uiState.pages.size,
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = SoptTheme.spacing.s14),
                )
            }

            Spacer(modifier = Modifier.height(SoptTheme.spacing.s24))

            Text(
                text = "화면을 밑으로 당기면 다른 친구를 볼 수 있어요",
                style = SoptTheme.typography.label3,
                color = SoptTheme.colors.fg.neutral.subtle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(SoptTheme.spacing.s16))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1012)
@Composable
private fun PokeOnboardingScreenPreview() {
    SoptTheme {
        PokeOnboardingScreen(
            uiState = PokeOnboardingUiState(
                pages = persistentListOf(
                    PokeRecommendationUiState(
                        randomType = "ALL",
                        title = "나와 같은 37기를 하고 있어요",
                        users = persistentListOf(
                            PokeUserUiState(
                                userId = 1,
                                userName = "커비",
                                userGeneration = 38,
                                userPart = "디자인",
                                profileImageUrl = null,
                                relationName = "친구",
                            ),
                            PokeUserUiState(
                                userId = 2,
                                userName = "박메이커",
                                userGeneration = 38,
                                userPart = "기획",
                                profileImageUrl = null,
                                relationName = "친구",
                                isPokeButtonEnabled = false,
                            ),
                            PokeUserUiState(
                                userId = 3,
                                userName = "커비",
                                userGeneration = 38,
                                userPart = "디자인",
                                profileImageUrl = null,
                                relationName = "친구",
                            ),
                            PokeUserUiState(
                                userId = 4,
                                userName = "박메이커",
                                userGeneration = 38,
                                userPart = "기획",
                                profileImageUrl = null,
                                relationName = "친구",
                                isPokeButtonEnabled = false,
                            ),
                            PokeUserUiState(
                                userId = 5,
                                userName = "커비",
                                userGeneration = 38,
                                userPart = "디자인",
                                profileImageUrl = null,
                                relationName = "친구",
                            ),
                            PokeUserUiState(
                                userId = 6,
                                userName = "박메이커",
                                userGeneration = 38,
                                userPart = "기획",
                                profileImageUrl = null,
                                relationName = "친구",
                                isPokeButtonEnabled = false,
                            ),
                        ),
                    ),
                    PokeRecommendationUiState(
                        randomType = "PART",
                        title = "같은 파트일 수도 있어요",
                        users = persistentListOf(),
                    ),
                ),
            ),
            onRefresh = {},
            onProfileClick = {},
            onPokeClick = {},
        )
    }
}
