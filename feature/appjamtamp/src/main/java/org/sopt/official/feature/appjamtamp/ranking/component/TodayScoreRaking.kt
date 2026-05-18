/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.appjamtamp.ranking.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.feature.appjamtamp.ranking.model.TopMissionScoreUiModel

@Composable
internal fun TodayScoreRaking(
    topMissionScore: TopMissionScoreUiModel,
    modifier: Modifier = Modifier,
    onTeamRankingClick: (teamNumber: String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 10.dp))
            .background(color = SoptTheme.colors.onSurface900)
            .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
            .noRippleClickable {
                onTeamRankingClick(topMissionScore.teamNumber)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = topMissionScore.teamName,
                color = White,
                style = SoptTheme.typography.heading16B,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        Spacer(modifier = Modifier.height(height = 14.dp))

        Text(
            text = "총 ${topMissionScore.totalPoints}점",
            color = SoptTheme.colors.onSurface300,
            style = SoptTheme.typography.title14SB,
            modifier = Modifier.align(Alignment.End)
        )

        Text(
            text = "+${topMissionScore.todayPoints}점",
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
            teamName = "키어로",
            teamNumber = "FIRST",
            todayPoints = 1000,
            totalPoints = 3000
        )

        TodayScoreRaking(topMissionScore = mockTopMissionScoreUiModel)
    }
}
