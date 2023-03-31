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

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.sopt.official.stamp.designsystem.style.MontserratBold
import org.sopt.official.stamp.designsystem.style.SoptTheme

@Composable
fun RankNumber(
    modifier: Modifier = Modifier,
    rank: Int,
    isMyRankNumber: Boolean = false
) {
    val defaultRankSymbols = "-"
    Text(
        text = if (rank <= 0) defaultRankSymbols else "$rank",
        fontFamily = MontserratBold,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = if (isMyRankNumber) SoptTheme.colors.purple300 else getRankTextColor(rank),
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun PreviewRankNumber() {
    SoptTheme {
        Column {
            RankNumber(rank = 1)
            RankNumber(rank = 2)
            RankNumber(rank = 3)
            RankNumber(rank = 4)
            RankNumber(rank = 5)
            RankNumber(rank = 6)
            RankNumber(rank = 7)
            RankNumber(rank = 8)
            RankNumber(rank = 9)
            RankNumber(rank = 10)
            RankNumber(rank = 11)
            RankNumber(rank = 12)
        }
    }
}
