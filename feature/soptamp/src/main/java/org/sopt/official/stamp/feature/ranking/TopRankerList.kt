/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.component.button.SoptampIconButton
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.feature.ranking.model.RankerUiModel
import org.sopt.official.stamp.feature.ranking.model.RankersUiModel
import org.sopt.official.stamp.feature.ranking.model.TopRankerDescriptionBubble

@Composable
fun TopRankerList(topRanker: RankersUiModel, onClickTopRankerBubble: (RankerUiModel) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 6.dp,
                bottom = 11.dp,
                start = 6.dp,
                end = 6.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var onClickRankerState by remember {
            mutableStateOf(topRanker.first)
        }
        if (onClickRankerState.rank > 0) {
            TopRankDescriptionBubble(
                bubble = TopRankerDescriptionBubble.findBubbleByRank(onClickRankerState.rank),
                onClickRankerDescriptionState = onClickRankerState.getDescription(),
                onClickItem = { onClickTopRankerBubble(onClickRankerState) }
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                20.dp,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.Bottom
        ) {
            val onClickRanker = { ranker: RankerUiModel ->
                onClickRankerState = ranker
            }
            TopRankerItem(
                ranker = topRanker.second,
                height = 110.dp,
                onClick = onClickRanker,
                onClickTopRankerBubble = { onClickTopRankerBubble(topRanker.second) }
            )
            TopRankerItem(
                ranker = topRanker.first,
                height = 150.dp,
                onClick = onClickRanker,
                onClickTopRankerBubble = { onClickTopRankerBubble(topRanker.first) }
            )
            TopRankerItem(
                ranker = topRanker.third,
                height = 70.dp,
                onClick = onClickRanker,
                onClickTopRankerBubble = { onClickTopRankerBubble(topRanker.third) }
            )
        }
    }
}

@Composable
fun TopRankDescriptionBubble(bubble: TopRankerDescriptionBubble, onClickRankerDescriptionState: String, onClickItem: () -> Unit = {}) {
    Box(
        modifier = Modifier.noRippleClickable {
            onClickItem()
        }
    ) {
        Icon(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = bubble.background),
            contentDescription = "Top Ranker DescriptionBubble",
            tint = bubble.backgroundColor
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 3.dp,
                    bottom = 13.dp,
                    start = 24.dp,
                    end = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val maxLength = 19
            val description = if (onClickRankerDescriptionState.length > maxLength) {
                "${onClickRankerDescriptionState.substring(0 until maxLength)}..."
            } else {
                onClickRankerDescriptionState
            }
            Text(
                text = description,
                style = SoptTheme.typography.sub3,
                color = bubble.textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            SoptampIconButton(
                imageVector = ImageVector.vectorResource(id = R.drawable.right_forward),
                tint = Color.White,
                onClick = { onClickItem() }
            )
        }
    }
}

@Preview
@Composable
fun PreviewTopRankerList() {
    SoptTheme {
        TopRankerList(
            topRanker = RankersUiModel(
                RankerUiModel(
                    rank = 1,
                    nickname = "jinsu",
                    description = "일이삼사육칠팔구십일이삼사오육칠팔구십dlfdl",
                    score = 1000

                ),
                RankerUiModel(
                    rank = 2,
                    nickname = "jinsu",
                    score = 900
                ),
                RankerUiModel(
                    rank = 3,
                    nickname = "jinsu",
                    score = 800
                )
            )
        )
    }
}
