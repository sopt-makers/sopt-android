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
package org.sopt.official.feature.appjamtamp.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.appjamtamp.R
import org.sopt.official.feature.appjamtamp.component.Toolbar

private enum class OnBoardingPageUiModel(
    @field:DrawableRes val image: Int,
    val title: String,
    val content: String,
) {
    FIRST(
        image = R.drawable.ic_onboarding_1,
        title = "A부터 Z까지 SOPT 즐기기",
        content = "동아리 활동을 더욱 재미있게\n즐기는 방법을 알려드려요!",
    ),
    SECOND(
        image = R.drawable.ic_onboarding_2,
        title = "랭킹으로 다같이 참여하기",
        content = "미션을 달성하고 랭킹이 올라가는\n재미를 느껴보세요!",
    ),
    THIRD(
        image = R.drawable.ic_onboarding_3,
        title = "완료된 미션으로 추억 감상하기",
        content = "완료된 미션을 확인하며\n추억을 감상할 수 있어요",
    ),
}

interface SimpleNavigator {
    fun popBackStack()
}

@Composable
fun OnboardingScreen(navigator: SimpleNavigator) {
    val onboardingPages = OnBoardingPageUiModel.entries.toTypedArray()
    val pageState = rememberPagerState { onboardingPages.size }
    SoptTheme {
        Column (
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Toolbar(
                modifier = Modifier.padding(bottom = 10.dp),
                title = {
                    Text(
                        text = "가이드",
                        style = SoptTheme.typography.heading18B,
                        modifier = Modifier.padding(start = 4.dp),
                        color = SoptTheme.colors.onSurface10,
                    )
                },
                onBack = navigator::popBackStack,
            )
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = pageState,
            ) {
                OnboardingPage(
                    image = onboardingPages[pageState.currentPage].image,
                    title = onboardingPages[pageState.currentPage].title,
                    content = onboardingPages[pageState.currentPage].content,
                )
            }
            Spacer(modifier = Modifier.size(28.dp))
            PageIndicator(
                numberOfPages = onboardingPages.size,
                selectedPage = pageState.currentPage,
                defaultRadius = 4.dp,
                defaultColor = SoptTheme.colors.onSurface500,
                selectedColor = SoptTheme.colors.primary,
                selectedLength = 20.dp,
                space = 10.dp,
                animationDurationInMillis = 100,
            )
            Spacer(modifier = Modifier.weight(1f))
            OnboardingButton(
                isButtonEnabled = (pageState.currentPage + 1 == onboardingPages.size),
                onClick = navigator::popBackStack,
                modifier =
                    Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 40.dp),
            )
        }
    }
}

@Composable
fun OnboardingButton(
    modifier: Modifier = Modifier,
    isButtonEnabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        enabled = isButtonEnabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = SoptTheme.colors.primary,
                disabledContainerColor = SoptTheme.colors.onSurface800,
            ),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(9.dp),
    ) {
        Text(
            text = "확인",
            color = if (isButtonEnabled) SoptTheme.colors.onSurface else SoptTheme.colors.onSurface300,
            style = SoptTheme.typography.heading18B,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    SoptTheme {
        OnboardingScreen(
            navigator =
                object : SimpleNavigator {
                    override fun popBackStack() {}
                },
        )
    }
}

