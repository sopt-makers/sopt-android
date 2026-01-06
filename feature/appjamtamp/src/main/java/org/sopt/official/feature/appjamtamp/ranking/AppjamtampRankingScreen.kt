package org.sopt.official.feature.appjamtamp.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.designsystem.component.indicator.LoadingIndicator
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.component.BackButtonHeader
import org.sopt.official.feature.appjamtamp.ranking.component.TodayScoreRaking
import org.sopt.official.feature.appjamtamp.ranking.component.Top3RecentRankingMission
import org.sopt.official.feature.appjamtamp.ranking.model.AppjamtampRankingState
import org.sopt.official.feature.appjamtamp.ranking.model.Top10MissionScoreListUiModel
import org.sopt.official.feature.appjamtamp.ranking.model.Top3RecentRankingListUiModel
import org.sopt.official.feature.appjamtamp.ranking.model.Top3RecentRankingUiModel
import org.sopt.official.feature.appjamtamp.ranking.model.TopMissionScoreUiModel

@Composable
internal fun AppjamtampRankingRoute(
    viewModel:AppjamtampRankingViewModel= hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getRankingData()
    }

    when(state) {
        is AppjamtampRankingState.Loading -> { LoadingIndicator() }
        is AppjamtampRankingState.Success -> {
            val top3RecentRankingList = (state as AppjamtampRankingState.Success).top3RecentRankingListUiModel.top3RecentRankingList
            val top10MissionScoreList = (state as AppjamtampRankingState.Success).top10MissionScoreListUiModel.top10MissionScoreList
            AppjamtampRankingScreen(
                top3RecentRankings = top3RecentRankingList,
                top10MissionScores = top10MissionScoreList
            )
        }
        is AppjamtampRankingState.Failure -> {}
    }
}

@Composable
internal fun AppjamtampRankingScreen(
    top3RecentRankings: ImmutableList<Top3RecentRankingUiModel>,
    top10MissionScores: ImmutableList<TopMissionScoreUiModel>
) {
    val scrollState = rememberScrollState()

    val density = LocalDensity.current
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidthDp = with(receiver = density) { screenWidthPx.toDp() }
    val topRankingItemWidth = screenWidthDp * (146f / 360f)

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            BackButtonHeader(
                title = "앱잼팀 랭킹",
                onBackButtonClick = {
                    // TODO - 뒤로가기 (앱잼탬프 홈 - AppjamtampMissionScreen)
                },
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(start = 16.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = SoptTheme.colors.onSurface950)
                .padding(paddingValues = paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "따끈따끈 합숙미션 구경하기",
                    color = White,
                    style = SoptTheme.typography.heading20B
                )

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_fire),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .padding(start = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(height = 8.dp))

            Text(
                text = "다른 팀들은 방금 이런 미션을 달성했어요",
                color = SoptTheme.colors.onSurface200,
                style = SoptTheme.typography.body13M
            )

            Spacer(modifier = Modifier.height(height = 20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(state = rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                top3RecentRankings.forEach { top3RecentRanking ->
                    Top3RecentRankingMission(
                        top3RecentRanking = top3RecentRanking,
                        modifier = Modifier.width(topRankingItemWidth)
                    )
                }
            }

            Spacer(modifier = Modifier.height(height = 40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "오늘의 득점 랭킹",
                    color = White,
                    style = SoptTheme.typography.heading20B
                )

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_medal),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .padding(start = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(height = 8.dp))

            Text(
                text = "미션을 인증해 오늘의 순위를 뒤집어보세요!",
                color = SoptTheme.colors.onSurface200,
                style = SoptTheme.typography.body13M
            )

            Spacer(modifier = Modifier.height(height = 20.dp))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 56.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp),
                maxItemsInEachRow = 2
            ) {
                top10MissionScores.forEach { item ->
                    TodayScoreRaking(
                        top10MissionScore = item,
                        modifier = Modifier.weight(1f),
                        onTeamRankingClick = {
                            // Todo : 앱잼 팀 미션 화면으로 이동 (teamNumber 전달)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppjamtampRankingScreenPreview() {
    SoptTheme {
        val mockTop3RecentRankingListUiModel = Top3RecentRankingListUiModel(
            top3RecentRankingList = persistentListOf(
                Top3RecentRankingUiModel(
                    stampId = 1L,
                    missionId = 101L,
                    userId = 1L,
                    imageUrl = "",
                    createdAt = "2025-12-31T05:03:49",
                    userName = "솝트",
                    userProfileImage = null,
                    teamName = "보핏",
                    teamNumber = "FIRST"
                ),
                Top3RecentRankingUiModel(
                    stampId = 2L,
                    missionId = 102L,
                    userId = 2L,
                    imageUrl = "",
                    createdAt = "2025-12-31T04:50:12",
                    userName = "안드",
                    userProfileImage = "",
                    teamName = "노바",
                    teamNumber = "SECOND"
                ),
                Top3RecentRankingUiModel(
                    stampId = 3L,
                    missionId = 103L,
                    userId = 3L,
                    imageUrl = "",
                    createdAt = "2025-12-31T03:20:00",
                    userName = "안드로이드",
                    userProfileImage = null,
                    teamName = "하이링구얼",
                    teamNumber = "THIRD"
                )
            )
        )

        val mockTop10MissionScoreListUiModel = Top10MissionScoreListUiModel(
            top10MissionScoreList = persistentListOf(
                TopMissionScoreUiModel(rank = 1, teamName = "보핏", todayPoints = 1200, totalPoints = 5000),
                TopMissionScoreUiModel(rank = 2, teamName = "노바", todayPoints = 1100, totalPoints = 4800),
                TopMissionScoreUiModel(rank = 3, teamName = "비트", todayPoints = 950, totalPoints = 4200),
                TopMissionScoreUiModel(rank = 4, teamName = "하이링구얼", todayPoints = 800, totalPoints = 3900),
                TopMissionScoreUiModel(rank = 5, teamName = "납작마켓", todayPoints = 750, totalPoints = 3500),
                TopMissionScoreUiModel(rank = 6, teamName = "웹", todayPoints = 600, totalPoints = 3100),
                TopMissionScoreUiModel(rank = 7, teamName = "안드로이드", todayPoints = 550, totalPoints = 2800),
                TopMissionScoreUiModel(rank = 8, teamName = "iOS", todayPoints = 400, totalPoints = 2500),
                TopMissionScoreUiModel(rank = 9, teamName = "디자인", todayPoints = 300, totalPoints = 2000),
                TopMissionScoreUiModel(rank = 10, teamName = "기획", todayPoints = 100, totalPoints = 1500)
            )
        )

        AppjamtampRankingScreen(
            top3RecentRankings = mockTop3RecentRankingListUiModel.top3RecentRankingList,
            top10MissionScores = mockTop10MissionScoreListUiModel.top10MissionScoreList
        )
    }
}
