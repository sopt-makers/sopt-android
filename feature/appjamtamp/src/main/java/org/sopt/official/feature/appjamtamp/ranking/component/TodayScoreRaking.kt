package org.sopt.official.feature.appjamtamp.ranking.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.ranking.model.TopMissionScoreUiModel
import org.sopt.official.feature.appjamtamp.util.noRippleClickable

@Composable
internal fun TodayScoreRaking(
    top10MissionScore: TopMissionScoreUiModel,
    modifier: Modifier = Modifier,
    onTeamRankingClick: (teamNumber: String) -> Unit = {}
) {
    val rankIconRes = when (top10MissionScore.rank) {
        1 -> R.drawable.ic_rank_1
        2 -> R.drawable.ic_rank_2
        3 -> R.drawable.ic_rank_3
        else -> R.drawable.ic_ranking_default
    }
    val rankTextColor = if (top10MissionScore.rank <= 3) Color.White else SoptTheme.colors.onSurface100

    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 10.dp))
            .background(color = SoptTheme.colors.onSurface900)
            .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 8.dp)
            .noRippleClickable {
                onTeamRankingClick("")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = rankIconRes),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    text = top10MissionScore.rank.toString(),
                    style = SoptTheme.typography.heading16B,
                    color = rankTextColor,
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 9.dp)
                )
            }

            Text(
                text = top10MissionScore.teamName,
                color = White,
                style = SoptTheme.typography.heading16B,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .padding(start = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(height = 14.dp))

        Text(
            text = "총 ${top10MissionScore.totalPoints}점",
            color = SoptTheme.colors.onSurface300,
            style = SoptTheme.typography.title14SB,
            modifier = Modifier.align(Alignment.End)
        )

        Text(
            text = "+${top10MissionScore.todayPoints}점",
            color = White,
            style = SoptTheme.typography.heading20B,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Preview
@Composable
private fun TodayScoreRakingPreview() {
    SoptTheme {
        val mockTopMissionScoreUiModel = TopMissionScoreUiModel(
            rank = 2,
            teamName = "노바",
            todayPoints = 1000,
            totalPoints = 3000
        )

        TodayScoreRaking(top10MissionScore = mockTopMissionScoreUiModel)
    }
}
