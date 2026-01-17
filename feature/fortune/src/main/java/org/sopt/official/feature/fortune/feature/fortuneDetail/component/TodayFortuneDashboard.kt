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
package org.sopt.official.feature.fortune.feature.fortuneDetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.fortune.R.drawable.img_fortune_title_small

@Composable
fun TodayFortuneDashboard(
    date: String,
    todaySentence: String,
    name: String,
    modifier: Modifier = Modifier,
) {
    TodayFortuneBox(
        content = {
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = modifier,
            ) {
                Spacer(modifier = Modifier.height(height = 32.dp))
                Image(
                    imageVector = ImageVector.vectorResource(id = img_fortune_title_small),
                    contentDescription = "오늘의 솝마디",
                )
                Spacer(modifier = Modifier.height(height = 10.dp))
                Text(
                    text = date,
                    style = typography.title18SB,
                    color = colors.onSurface100,
                )
                Spacer(modifier = Modifier.height(height = 20.dp))
                TodayFortuneText(
                    todaySentence = todaySentence,
                    name = name,
                )
                Spacer(modifier = Modifier.height(height = 36.dp))
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun TodayFortuneDashboardPreview() {
    SoptTheme {
        TodayFortuneDashboard(
            date = "2024-09-09",
            todaySentence = "예상치 못한 칭찬을 받게 되겠솝",
            name = "김세훈",
        )
    }
}
