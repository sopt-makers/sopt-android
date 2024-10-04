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
import org.sopt.official.feature.fortune.feature.fortuneDetail.component.FortuneDetailButton
import org.sopt.official.feature.fortune.feature.fortuneDetail.component.PokeRecommendationDashboard
import org.sopt.official.feature.fortune.feature.fortuneDetail.component.TodayFortuneDashboard
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Error
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Loading
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success.TodaySentence
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success.UserInfo
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.TodaySentence
import timber.log.Timber

@Composable
internal fun FortuneDetailScreen(
    paddingValue: PaddingValues,
    date: String,
    onFortuneAmuletClick: () -> Unit,
    onPokeClick: (userId: Long) -> Unit,
    onProfileClick: (userId: Long) -> Unit,
    modifier: Modifier = Modifier,
    uiState: FortuneDetailUiState = Loading,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValue)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 16.dp))
        when (uiState) {
            is Success -> {
                TodayFortuneDashboard(
                    date = date,
                    todaySentence = uiState.todaySentence.message,
                )
                Spacer(modifier = Modifier.height(height = 20.dp))
                PokeRecommendationDashboard(
                    profile = uiState.userInfo.profile,
                    name = uiState.userInfo.userName,
                    userDescription = uiState.userInfo.userDescription,
                    onPokeClick = { onPokeClick(uiState.userInfo.userId) },
                    onProfileClick = { onProfileClick(uiState.userInfo.userId) },
                )
                Spacer(modifier = Modifier.weight(1f))
                FortuneDetailButton(
                    onButtonClick = onFortuneAmuletClick,
                    buttonTitle = "오늘의 부적 받기"
                )
            }

            is Error -> Timber.e(uiState.errorMessage)
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
            uiState = Success(
                todaySentence = TodaySentence(
                    userName = "이현우",
                    content = "사과해요나한테사과해요나한테사과해요나한테"
                ),
                userInfo = UserInfo(
                    userId = 0L,
                    profile = "",
                    userName = "동민",
                    generation = 111,
                    part = "기획 파트"
                )
            ),
            onPokeClick = { },
            onProfileClick = { },
        )
    }
}
