package org.sopt.official.stamp.feature.ranking.part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.analytics.EventType
import org.sopt.official.stamp.LocalTracker
import org.sopt.official.stamp.config.navigation.MissionNavGraph
import org.sopt.official.stamp.designsystem.component.dialog.SingleOptionDialog
import org.sopt.official.stamp.designsystem.component.layout.LoadingScreen
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.feature.destinations.RankingScreenDestination
import org.sopt.official.stamp.feature.ranking.RankListItem
import org.sopt.official.stamp.feature.ranking.RankingBar
import org.sopt.official.stamp.feature.ranking.TopRankBarOfRankText
import org.sopt.official.stamp.feature.ranking.model.PartRankModel
import org.sopt.official.stamp.feature.ranking.rank.RankingHeader

@MissionNavGraph
@Destination("partRanking")
@Composable
fun PartRankingScreen(
    partRankingViewModel: PartRankingViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    navigator: DestinationsNavigator
) {
    val state by partRankingViewModel.state.collectAsState()
    val tracker = LocalTracker.current
    LaunchedEffect(true) {
        tracker.track(
            EventType.VIEW,
            "partranking"
        )
        partRankingViewModel.fetchRanking()
    }

    SoptTheme {
        when (state) {
            PartRankingState.Loading -> LoadingScreen()
            PartRankingState.Failure -> SingleOptionDialog {
                partRankingViewModel.fetchRanking()
            }

            is PartRankingState.Success -> PartRankingScreen(
                partRankList = (state as PartRankingState.Success).partRankList,
                refreshing = partRankingViewModel.isRefreshing,
                onRefresh = partRankingViewModel::onRefresh,
                onClickBack = resultNavigator::navigateBack,
                onClickPart = {
                    navigator.navigate(RankingScreenDestination(it))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PartRankingScreen(
    partRankList: ImmutableList<PartRankModel>,
    refreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onClickBack: () -> Unit = {},
    onClickPart: (String) -> Unit = {}
) {
    val refreshingState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = onRefresh
    )
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            RankingHeader(
                title = "파트별 랭킹",
                onClickBack = onClickBack
            )
        }
    ) { paddingValues ->
        val defaultPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    PartRankingBarList(partRankList)
                }
                items(partRankList.sortedBy { it.rank }) { item ->
                    RankListItem(
                        partItem = item,
                        onClickPart = {
                            onClickPart(item.part)
                        }
                    )
                }
            }
            PullRefreshIndicator(refreshing, refreshingState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
fun PartRankingBarList(rankList: List<PartRankModel>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.Bottom
    ) {
        items(rankList) { part ->
            PartRankingBar(part = part)
        }
    }
}

@Composable
fun PartRankingBar(part: PartRankModel, modifier: Modifier = Modifier) {
    val newRank = if (part.point != 0) {
        part.rank
    } else {
        0
    }

    Column(
        modifier = Modifier.defaultMinSize(minHeight = getRankHeight(1)),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (part.rank < 4 && part.point != 0) {
            TopRankBarOfRankText(rank = part.rank)
        }
        RankingBar(
            modifier = modifier.size(width = 50.dp, height = getRankHeight(newRank)),
            rank = newRank
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = part.part,
            maxLines = 1,
            style = SoptTheme.typography.sub3,
            color = SoptTheme.colors.onSurface80
        )
    }
}

fun getRankHeight(rank: Int) = when (rank) {
    1 -> 163.dp
    2 -> 135.dp
    3 -> 108.dp
    4 -> 81.dp
    5 -> 54.dp
    else -> 27.dp
}

@Preview
@Composable
fun PartRankingPreview() {
    val partRankList = persistentListOf(
        PartRankModel(
            3,
            "기획",
            500
        ),
        PartRankModel(
            4,
            "디자인",
            400
        ),
        PartRankModel(
            6,
            "웹",
            100
        ),
        PartRankModel(
            2,
            "아요",
            1000
        ),
        PartRankModel(
            1,
            "안드",
            8000
        ),
        PartRankModel(
            5,
            "서버",
            200
        )
    )
    SoptTheme {
        PartRankingScreen(partRankList)
    }
}
