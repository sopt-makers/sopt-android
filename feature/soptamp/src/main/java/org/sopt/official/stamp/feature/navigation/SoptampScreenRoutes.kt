/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import dev.zacsweers.metro.viewmodel.compose.metroViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.domain.soptamp.error.Error
import org.sopt.official.domain.soptamp.model.MissionsFilter
import org.sopt.official.model.UserStatus
import org.sopt.official.stamp.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.stamp.designsystem.component.layout.LoadingScreen
import org.sopt.official.stamp.feature.mission.MissionsState
import org.sopt.official.stamp.feature.mission.MissionsViewModel
import org.sopt.official.stamp.feature.mission.detail.MissionDetailScreen
import org.sopt.official.stamp.feature.mission.detail.MissionDetailViewModel
import org.sopt.official.stamp.feature.mission.list.MissionListScreenNew
import org.sopt.official.stamp.feature.mission.list.UserMissionListScreen
import org.sopt.official.stamp.feature.mission.model.MissionNavArgs
import org.sopt.official.stamp.feature.onboarding.OnboardingScreenNew
import org.sopt.official.stamp.feature.ranking.part.PartRankingScreen
import org.sopt.official.stamp.feature.ranking.part.PartRankingState
import org.sopt.official.stamp.feature.ranking.part.PartRankingViewModel
import org.sopt.official.stamp.feature.ranking.rank.RankingScreen
import org.sopt.official.stamp.feature.ranking.rank.RankingState
import org.sopt.official.stamp.feature.ranking.rank.RankingViewModel

@Composable
fun MissionListScreenRoute(navController: NavController) {
    val missionsViewModel: MissionsViewModel = metroViewModel()
    val resultFlow = navController.getMissionDetailResult()
    val result by resultFlow.collectAsStateWithLifecycle()

    LaunchedEffect(result) {
        result?.let { isRefreshNeeded ->
            if (isRefreshNeeded) {
                missionsViewModel.fetchMissions()
            }
            navController.clearMissionDetailResult()
        }
    }

    // Call the refactored MissionListScreen
    MissionListScreenNew(
        navController = navController,
        missionsViewModel = missionsViewModel,
    )
}

@Composable
fun MissionDetailScreenRoute(
    args: MissionDetail,
    navController: NavController,
) {
    val viewModel: MissionDetailViewModel = metroViewModel()

    // Call the MissionDetailScreen with AndroidX Navigation
    MissionDetailScreen(
        args =
            MissionNavArgs(
                id = args.id,
                title = args.title,
                level = args.toMissionLevel(),
                isCompleted = args.isCompleted,
                isMe = args.isMe,
                nickname = args.nickname,
            ),
        navController = navController,
        viewModel = viewModel,
    )
}

@Composable
fun RankingScreenRoute(
    args: Ranking,
    navController: NavController,
) {
    val rankingViewModel: RankingViewModel = metroViewModel()
    val context = LocalContext.current
    val state by rankingViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        val isCurrent = args.type.first().isDigit()
        rankingViewModel.fetchRanking(isCurrent, args.type)
    }

    when (state) {
        RankingState.Loading -> LoadingScreen()
        RankingState.Failure ->
            NetworkErrorDialog {
                navController.popBackStack()
            }

        is RankingState.Success -> {
            val successState = state as RankingState.Success
            RankingScreen(
                entrySource = args.entrySource,
                isCurrent = args.type.first().isDigit(),
                type = args.type,
                refreshing = rankingViewModel.isRefreshing,
                onRefresh = {
                    val isCurrent = args.type.first().isDigit()
                    rankingViewModel.onRefresh(isCurrent, args.type)
                },
                rankingListUiModel = successState.uiModel,
                nickname = successState.nickname,
                onClickBack = { navController.popBackStack() },
                onClickUser = { ranker ->
                    navController.navigateToUserMissionList(ranker)
                },
                navigateToMyPageStamp = {
                    val intent = DeepLinkType.MY_PAGE_SOPTAMP.getIntent(
                        context = context,
                        userStatus = UserStatus.UNAUTHENTICATED, // Todo : 유저 상태에 맞게 변경하기
                        deepLink = DeepLinkType.MY_PAGE_SOPTAMP.link
                    )
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun PartRankingScreenRoute(navController: NavController) {
    val context = LocalContext.current
    val partRankingViewModel: PartRankingViewModel = metroViewModel()
    val state by partRankingViewModel.state.collectAsStateWithLifecycle()
    val partRankingEntrySource = "partRanking"

    LaunchedEffect(true) {
        partRankingViewModel.fetchRanking()
    }

    when (state) {
        PartRankingState.Loading -> LoadingScreen()
        PartRankingState.Failure ->
            NetworkErrorDialog {
                partRankingViewModel.fetchRanking()
            }

        is PartRankingState.Success -> {
            val successState = state as PartRankingState.Success
            PartRankingScreen(
                partRankList = successState.partRankList,
                refreshing = partRankingViewModel.isRefreshing,
                onRefresh = partRankingViewModel::onRefresh,
                onClickBack = { navController.popBackStack() },
                onClickPart = { partName ->
                    navController.navigateToRanking(partName, partRankingEntrySource)
                },
                navigateToMyPageStamp = {
                    val intent = DeepLinkType.MY_PAGE_SOPTAMP.getIntent(
                        context = context,
                        userStatus = UserStatus.UNAUTHENTICATED, // Todo : 유저 상태에 맞게 변경하기
                        deepLink = DeepLinkType.MY_PAGE_SOPTAMP.link
                    )
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun UserMissionListScreenRoute(
    args: UserMissionList,
    navController: NavController,
) {
    val missionsViewModel: MissionsViewModel = metroViewModel()
    val state by missionsViewModel.state.collectAsStateWithLifecycle()
    val myNickname by missionsViewModel.nickname.collectAsStateWithLifecycle()
    val description by missionsViewModel.description.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        missionsViewModel.fetchMissions(
            filter = MissionsFilter.COMPLETE_MISSION.title,
            nickname = args.nickname,
        )
    }

    when (state) {
        MissionsState.Loading -> LoadingScreen()
        is MissionsState.Failure -> {
            when ((state as MissionsState.Failure).error) {
                Error.NetworkUnavailable ->
                    NetworkErrorDialog(
                        onRetry = missionsViewModel::fetchMissions,
                    )
            }
        }

        is MissionsState.Success -> {
            val successState = state as MissionsState.Success
            UserMissionListScreen(
                myName = myNickname,
                entrySource = args.entrySource,
                userName = args.nickname,
                description = if (args.nickname == myNickname) description else args.description ?: "설정된 한 마디가 없습니다.",
                missionListUiModel = successState.missionListUiModel,
                isMe = args.nickname == myNickname,
                onMissionItemClick = { item ->
                    navController.navigateToMissionDetail(item)
                },
                onClickBack = { navController.popBackStack() },
            )
        }
    }
}

@Composable
fun OnboardingScreenRoute(navController: NavController) {
    OnboardingScreenNew(navController = navController)
}
