package org.sopt.official.stamp.feature.ranking.part

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.Gray500
import org.sopt.official.stamp.designsystem.style.Gray800
import org.sopt.official.stamp.designsystem.style.MontserratBold
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.feature.ranking.RankScore
import org.sopt.official.stamp.feature.ranking.TopRankBarOfRankText
import org.sopt.official.stamp.feature.ranking.getRankBackgroundColor
import org.sopt.official.stamp.feature.ranking.model.PartRankModel
import org.sopt.official.stamp.feature.ranking.rank.RankingHeader

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PartRankingScreen(
    refreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onClickBack: () -> Unit = {}
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
                onClickBack = { onClickBack() }
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
                items(partRankList.sortedBy { it.rank }) { item -> // 리스트를 받으면 넣을 예정
                    PartRankListItem(
                        item = item,
                        onClickUser = {
                            // 페이지 이동
                        }
                    )
                }
            }
            PullRefreshIndicator(refreshing, refreshingState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
fun PartRankingBarList(rankList: List<PartRankModel> = partRankList) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.Bottom
    ) {
        items(rankList) { part ->
            PartRankingBar(part)
        }
    }
}

@Composable
fun PartRankingBar(part: PartRankModel) {
    val newRank = if (part.point != 0) {
        part.rank
    } else {
        0
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (part.rank < 4 && part.point != 0) {
            TopRankBarOfRankText(rank = part.rank)
        }
        RankingBar(width = 50.dp, height = getRankHeight(newRank), rank = newRank)
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = part.part,
            maxLines = 1,
            style = SoptTheme.typography.sub3,
            color = Gray800
        )
    }
}

@Composable
fun RankingBar(width: Dp, height: Dp, rank: Int, content: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .size(width, height)
            .background(
                color = getRankBackgroundColor(rank),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        content()
    }
}

@Composable
fun PartRankListItem(item: PartRankModel, onClickUser: () -> Unit = {}) {
    val itemPadding = PaddingValues(
        top = 12.dp,
        bottom = 11.dp,
        start = 16.dp,
        end = 16.dp
    )
    val backgroundModifier = Modifier.background(
        color = SoptTheme.colors.onSurface5,
        shape = RoundedCornerShape(8.dp)
    )

    Row(
        modifier = backgroundModifier
            .fillMaxWidth()
            .noRippleClickable { onClickUser() }
            .padding(itemPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(0.18f)
        ) {
            Text(
                text = if (item.rank <= 0) "-" else "${item.rank}",
                fontFamily = MontserratBold,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Gray500,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.weight(0.05f))
        Box(
            modifier = Modifier.weight(0.53f)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item.part,
                style = SoptTheme.typography.h3,
                color = Gray800,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.weight(0.04f))
        Box(
            modifier = Modifier.weight(0.4f)
        ) {
            RankScore(
                modifier = Modifier.align(Alignment.CenterEnd),
                rank = -1,
                score = item.point,
            )
        }
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

val partRankList = listOf(
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
        1,
        "아요",
        10000
    ),
    PartRankModel(
        2,
        "안드",
        800
    ),
    PartRankModel(
        5,
        "서버",
        200
    ),
)

@Preview
@Composable
fun PartRankingPreview() {
    SoptTheme {
        PartRankingScreen()
    }
}