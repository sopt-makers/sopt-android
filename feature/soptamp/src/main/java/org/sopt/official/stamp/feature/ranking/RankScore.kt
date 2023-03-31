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

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.sopt.official.stamp.designsystem.style.MontserratRegular
import org.sopt.official.stamp.designsystem.style.PretendardMedium
import org.sopt.official.stamp.designsystem.style.SoptTheme

@Composable
fun RankScore(
    modifier: Modifier = Modifier,
    rank: Int,
    score: Int,
    isMyRankScore: Boolean = false
) {
    val textColor = if (isMyRankScore) {
        SoptTheme.colors.purple300
    } else {
        getRankTextColor(rank = rank)
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "$score",
            fontFamily = MontserratRegular,
            fontSize = 30.sp,
            fontWeight = FontWeight.W400,
            color = textColor
        )
        Text(
            text = "점",
            fontFamily = PretendardMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            color = textColor
        )
    }
}

@Preview
@Composable
fun PreviewRankScore() {
    SoptTheme {
        RankScore(rank = 1, score = 1000)
    }
}
