/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.feature.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch
import org.sopt.official.stamp.R
import org.sopt.official.stamp.config.navigation.MissionNavGraph
import org.sopt.official.stamp.designsystem.component.button.SoptampFloatingButton
import org.sopt.official.stamp.designsystem.component.button.SoptampIconButton
import org.sopt.official.stamp.designsystem.component.dialog.SingleOptionDialog
import org.sopt.official.stamp.designsystem.component.layout.LoadingScreen
import org.sopt.official.stamp.designsystem.component.topappbar.SoptTopAppBar
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.feature.destinations.UserMissionListScreenDestination
import org.sopt.official.stamp.feature.ranking.model.RankerNavArg
import org.sopt.official.stamp.feature.ranking.model.RankerUiModel
import org.sopt.official.stamp.feature.ranking.model.RankingListUiModel
import org.sopt.official.stamp.feature.ranking.model.toArgs
import org.sopt.official.stamp.util.toPx

@MissionNavGraph
@Destination("ranking")
@Composable
fun RankingScreen(
    rankingViewModel: RankingViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    navigator: DestinationsNavigator
) {
    val state by rankingViewModel.state.collectAsState()
    SoptTheme {
        when (state) {
            RankingState.Loading -> LoadingScreen()
            RankingState.Failure -> SingleOptionDialog {
                rankingViewModel.fetchRanking()
            }

            is RankingState.Success -> RankingScreen(
                refreshing = rankingViewModel.isRefreshing,
                onRefresh = { rankingViewModel.onRefresh() },
                rankingListUiModel = (state as RankingState.Success).uiModel,
                nickname = rankingViewModel.nickname,
                onClickBack = { resultNavigator.navigateBack() },
                onClickUser = { ranker -> navigator.navigate(UserMissionListScreenDestination(ranker)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RankingScreen(
    refreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    rankingListUiModel: RankingListUiModel,
    nickname: String,
    onClickBack: () -> Unit = {},
    onClickUser: (RankerNavArg) -> Unit = {}
) {
    val refreshingState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = onRefresh
    )
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val scrollOffsetPx = (-257).dp.toPx()
    Scaffold(
        topBar = {
            RankingHeader(
                title = "랭킹",
                onClickBack = { onClickBack() }
            )
        },
        floatingActionButton = {
            SoptampFloatingButton(
                text = "내 랭킹 보기"
            ) {
                coroutineScope.launch {
                    val currentUserIndex = rankingListUiModel.otherRankingList.withIndex()
                        .find { it.value.nickname == nickname }
                        ?.index
                        ?: 0
                    listState.animateScrollToItem(index = currentUserIndex, scrollOffset = scrollOffsetPx)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        val defaultPadding = PaddingValues(
            top = 0.dp,
            bottom = paddingValues.calculateBottomPadding(),
            start = 16.dp,
            end = 16.dp
        )
        Box(
            modifier = Modifier.pullRefresh(refreshingState)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(defaultPadding),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 70.dp)
            ) {
                item {
                    TopRankerList(
                        topRanker = rankingListUiModel.topRankingList,
                        onClickTopRankerBubble = { ranker -> onClickUser(ranker.toArgs()) }
                    )
                }
                items(rankingListUiModel.otherRankingList) { item ->
                    RankListItem(
                        item = item,
                        isMyRanking = item.nickname == nickname,
                        onClickUser = { ranker ->
                            if (nickname != ranker.nickname) onClickUser(ranker.toArgs())
                        }
                    )
                }
            }
            PullRefreshIndicator(refreshing, refreshingState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
fun RankingHeader(
    title: String,
    onClickBack: () -> Unit = {}
) {
    SoptTopAppBar(
        title = {
            Text(
                text = title,
                color = Color.Black,
                style = SoptTheme.typography.h2
            )
        },
        navigationIcon = {
            SoptampIconButton(
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_left),
                onClick = { onClickBack() }
            )
        }
    )
}

@Preview
@Composable
fun PreviewRankingScreen() {
    val previewRanking = mutableListOf<RankerUiModel>()
    repeat(100) {
        previewRanking.add(
            RankerUiModel(
                rank = it + 1,
                nickname = "jinsu",
                score = 1000 - (it * 10)
            )
        )
    }
    SoptTheme {
        RankingScreen(
            rankingListUiModel = RankingListUiModel(previewRanking),
            nickname = "",
        )
    }
}
