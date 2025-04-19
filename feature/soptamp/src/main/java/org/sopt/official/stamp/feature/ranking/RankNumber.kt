/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.stamp.designsystem.style.MontserratBold

@Composable
fun RankNumber(modifier: Modifier = Modifier, rank: Int, isMyRankNumber: Boolean = false, isPartRankNumber: Boolean = false) {
    val defaultRankSymbols = "-"
    Text(
        text = if (rank <= 0) defaultRankSymbols else "$rank",
        fontFamily = MontserratBold,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = when {
            isMyRankNumber -> SoptTheme.colors.onSurface300
            isPartRankNumber -> SoptTheme.colors.onSurface300
            rank == 1 -> SoptTheme.colors.primary
            else -> getRankTextColor(rank)
        },
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
