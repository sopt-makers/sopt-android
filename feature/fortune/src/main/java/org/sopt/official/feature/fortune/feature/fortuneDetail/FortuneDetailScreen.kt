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
package org.sopt.official.feature.fortune.feature.fortuneDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.feature.fortuneDetail.component.TodayFortuneDashboard
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Error
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Loading
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.TodaySentence

@Composable
internal fun FortuneDetailScreen(
    paddingValue: PaddingValues,
    date: String,
    onFortuneAmuletClick: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: FortuneDetailUiState = Loading,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValue),
    ) {
        Spacer(modifier = Modifier.height(height = 16.dp))
        when (uiState) {
            is TodaySentence -> {
                TodayFortuneDashboard(
                    date = date,
                    todaySentence = uiState.message,
                )
            }

            is Error -> {
                // 오류 처리
            }

            is Loading -> {
                // 로딩 뷰
            }
        }
    }
}

@Preview
@Composable
private fun FortuneDetailScreenPreview() {
    SoptTheme {
        FortuneDetailScreen(
            paddingValue = PaddingValues(vertical = 16.dp),
            date = "2024-09-09",
            onFortuneAmuletClick = {},
            uiState = TodaySentence(
                userName = "누누",
                content = "오늘 하루종일 기분 좋을 것 같은 날이네요."
            )
        )
    }
}
