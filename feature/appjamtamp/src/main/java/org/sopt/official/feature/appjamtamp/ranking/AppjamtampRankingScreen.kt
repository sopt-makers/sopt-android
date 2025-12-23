package org.sopt.official.feature.appjamtamp.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.component.BackButtonHeader
import org.sopt.official.feature.appjamtamp.ranking.component.TodayScoreRaking
import org.sopt.official.feature.appjamtamp.ranking.component.TopRankingTeamMission

@Composable
internal fun AppjamtampRankingScreen(

) {
    val scrollState = rememberScrollState()

    val density = LocalDensity.current
    val screenWidthPx = LocalWindowInfo.current.containerSize.width
    val screenWidthDp = with(receiver = density) { screenWidthPx.toDp() }
    val topRankingItemWidth = screenWidthDp * (146f / 375f)

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            BackButtonHeader(
                title = "앱잼팀 랭킹",
                onBackButtonClick = {}, // TODO - 뒤로가기
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
                repeat(times = 3) {
                    TopRankingTeamMission(
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

            val rankingList = List(10) { it } // TODO - 10개 표시하기 위한 임시 리스트
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 56.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                rankingList.chunked(size = 2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
                    ) {
                        rowItems.forEach { item ->
                            TodayScoreRaking(modifier = Modifier.weight(weight = 1f))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppjamtampRankingScreenPreview() {
    SoptTheme {
        AppjamtampRankingScreen()
    }
}
