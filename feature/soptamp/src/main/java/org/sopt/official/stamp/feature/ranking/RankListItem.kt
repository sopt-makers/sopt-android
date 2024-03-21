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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.feature.ranking.model.RankerUiModel

@Composable
fun RankListItem(item: RankerUiModel, isMyRanking: Boolean = false, onClickUser: (RankerUiModel) -> Unit = {}) {
    val itemPadding = PaddingValues(
        top = 19.dp,
        bottom = 16.dp,
        start = 14.dp,
        end = 14.dp
    )
    val backgroundModifier = if (isMyRanking) {
        Modifier
            .background(
                color = SoptTheme.colors.purple100,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 2.dp,
                color = SoptTheme.colors.purple300,
                shape = RoundedCornerShape(8.dp)
            )
    } else {
        Modifier.background(
            color = SoptTheme.colors.onSurface5,
            shape = RoundedCornerShape(8.dp)
        )
    }
    Row(
        modifier = backgroundModifier
            .fillMaxWidth()
            .noRippleClickable { onClickUser(item) }
            .padding(itemPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(0.18f)
        ) {
            RankNumber(
                modifier = Modifier.align(Alignment.Center),
                rank = item.rank,
                isMyRankNumber = isMyRanking
            )
        }
        Spacer(modifier = Modifier.weight(0.05f))
        Box(
            modifier = Modifier.weight(0.53f)
        ) {
            RankerInformation(
                user = item.nickname,
                description = item.getDescription()
            )
        }
        Spacer(modifier = Modifier.weight(0.04f))
        Box(
            modifier = Modifier.weight(0.2f)
        ) {
            RankScore(
                modifier = Modifier.align(Alignment.CenterEnd),
                rank = item.rank,
                score = item.score,
                isMyRankScore = isMyRanking
            )
        }
    }
}

@Composable
fun RankerInformation(modifier: Modifier = Modifier, user: String, description: String) {
    Column(modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = user,
            style = SoptTheme.typography.h3,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = description,
            style = SoptTheme.typography.caption1,
            color = SoptTheme.colors.onSurface70,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun PreviewRankListItem() {
    SoptTheme {
        Column(Modifier.padding(horizontal = 16.dp)) {
            RankListItem(
                item = RankerUiModel(
                    rank = 4,
                    nickname = "일이삼사오육칠팔구십일이삼사오육칠팔구십",
                    description = "일이삼사오육칠팔구십일이삼사오육칠팔구십",
                    score = 300
                ),
                true
            )
            Spacer(modifier = Modifier.size(10.dp))
            RankListItem(
                item = RankerUiModel(
                    rank = 10,
                    nickname = "일이삼사오육칠팔구십일이삼사오육칠팔구십",
                    description = "일이삼사오육칠팔구십일이삼사오육칠팔구십",
                    score = 340
                ),
                false
            )
            Spacer(modifier = Modifier.size(10.dp))
            RankListItem(
                item = RankerUiModel(
                    rank = 140,
                    nickname = "일이삼사오육칠팔구십일이삼사오육칠팔구십",
                    description = "일이삼사오육칠팔구십일이삼사오육칠팔구십",
                    score = 945
                ),
                false
            )
        }
    }
}
