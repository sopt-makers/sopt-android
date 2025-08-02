/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.ranking.rank

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.component.button.SoptampFloatingButton
import org.sopt.official.stamp.designsystem.component.button.SoptampIconButton
import org.sopt.official.stamp.designsystem.component.topappbar.SoptTopAppBar
import org.sopt.official.stamp.feature.ranking.RankListItem
import org.sopt.official.stamp.feature.ranking.TopRankerList
import org.sopt.official.stamp.feature.ranking.model.RankerNavArg
import org.sopt.official.stamp.feature.ranking.model.RankerUiModel
import org.sopt.official.stamp.feature.ranking.model.RankerUiModel.Companion.DEFAULT_USER_NAME
import org.sopt.official.stamp.feature.ranking.model.RankingListUiModel
import org.sopt.official.stamp.feature.ranking.model.toArgs
import org.sopt.official.stamp.util.toPx

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RankingScreen(
    isCurrent: Boolean,
    type: String,
    refreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    rankingListUiModel: RankingListUiModel,
    nickname: String,
    onClickBack: () -> Unit = {},
    onClickUser: (RankerNavArg) -> Unit = {},
) {
    val refreshingState =
        rememberPullRefreshState(
            refreshing = refreshing,
            onRefresh = onRefresh,
        )
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val scrollOffsetPx = (-257).dp.toPx()
    val tracker = LocalTracker.current

    LaunchedEffect(true) {
        tracker.track(
            EventType.VIEW,
            if (isCurrent) "nowranking" else "partdetailranking",
        )
    }

    Scaffold(
        topBar = {
            RankingHeader(
                title = if (isCurrent) "$type 랭킹" else type + "파트 랭킹",
                onClickBack = onClickBack,
            )
        },
        floatingActionButton = {
            val myIndex = rankingListUiModel.otherRankingList.withIndex().find { it.value.nickname == nickname }
            if (myIndex != null) {
                SoptampFloatingButton(
                    text = "내 랭킹 보기",
                ) {
                    tracker.track(EventType.CLICK, if (isCurrent) "myranking_nowranking" else "myranking_allranking")
                    coroutineScope.launch {
                        listState.animateScrollToItem(
                            index = myIndex.index,
                            scrollOffset = scrollOffsetPx,
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = SoptTheme.colors.onSurface950,
        modifier =
            Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) { paddingValues ->
        val defaultPadding =
            PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp,
            )
        Box(
            modifier =
                Modifier
                    .pullRefresh(refreshingState)
                    .fillMaxSize(),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(defaultPadding),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
            ) {
                item {
                    TopRankerList(
                        topRanker = rankingListUiModel.topRankingList,
                        onClickTopRankerBubble = { ranker ->
                            if (ranker.nickname != DEFAULT_USER_NAME) onClickUser(ranker.toArgs())
                        },
                    )
                }
                items(rankingListUiModel.otherRankingList) { item ->
                    RankListItem(
                        rankerItem = item,
                        isMyRanking = item.nickname == nickname,
                        onClickUser = { ranker ->
                            if (nickname != ranker.nickname) onClickUser(ranker.toArgs())
                        },
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
    onClickBack: () -> Unit = {},
) {
    SoptTopAppBar(
        title = {
            Text(
                text = title,
                color = SoptTheme.colors.onSurface10,
                style = SoptTheme.typography.heading18B,
            )
        },
        navigationIcon = {
            SoptampIconButton(
                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_left),
                tint = SoptTheme.colors.onSurface10,
                onClick = onClickBack,
            )
        },
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
                score = 1000 - (it * 10),
            ),
        )
    }
    SoptTheme {
        RankingScreen(
            isCurrent = true,
            type = "34기",
            rankingListUiModel = RankingListUiModel(previewRanking),
            nickname = "",
        )
    }
}
