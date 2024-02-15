/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
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
