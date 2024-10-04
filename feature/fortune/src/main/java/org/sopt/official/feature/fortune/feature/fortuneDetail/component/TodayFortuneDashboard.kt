/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray100
import org.sopt.official.designsystem.Gray30
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.R.drawable.img_fortune_title

@Composable
internal fun TodayFortuneDashboard(
    date: String,
    todaySentence: String,
    modifier: Modifier = Modifier,
) {
    TodayFortuneBox(
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier,
            ) {
                Spacer(modifier = Modifier.height(height = 32.dp))
                Image(
                    imageVector = ImageVector.vectorResource(img_fortune_title),
                    contentDescription = "오늘의 솝마디",
                )
                Spacer(modifier = Modifier.height(height = 10.dp))
                Text(
                    text = date,
                    style = SoptTheme.typography.title18SB,
                    color = Gray100,
                )
                Spacer(modifier = Modifier.height(height = 20.dp))
                Text(
                    text = todaySentence,
                    style = SoptTheme.typography.title24SB.copy(
                        lineBreak = LineBreak.Simple,
                    ),
                    color = Gray30,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 68.dp)
                        .semantics { contentDescription = "todaySentence" },
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
            todaySentence = "hi my name is Sehun kim, nice to meet you",
        )
    }
}
