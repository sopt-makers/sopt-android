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
package org.sopt.official.stamp.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.sopt.official.stamp.R
import org.sopt.official.stamp.config.navigation.MissionNavGraph
import org.sopt.official.stamp.designsystem.component.layout.SoptColumn
import org.sopt.official.stamp.designsystem.style.SoptTheme

private enum class OnBoardingPageUiModel(
    @DrawableRes val image: Int,
    val title: String,
    val content: String
) {
    FIRST(
        image = R.drawable.ic_onboarding_1,
        title = "A부터 Z까지 SOPT 즐기기",
        content = "동아리 활동을 더욱 재미있게\n즐기는 방법을 알려드려요!"
    ),
    SECOND(
        image = R.drawable.ic_onboarding_2,
        title = "랭킹으로 다같이 참여하기",
        content = "미션을 달성하고 랭킹이 올라가는\n재미를 느껴보세요!"
    ),
    THIRD(
        image = R.drawable.ic_onboarding_3,
        title = "완료된 미션으로 추억 감상하기",
        content = "완료된 미션을 확인하며\n추억을 감상할 수 있어요"
    );
}

@OptIn(ExperimentalFoundationApi::class)
@MissionNavGraph
@Destination("onboarding")
@Composable
fun OnboardingScreen(
    navigator: DestinationsNavigator
) {
    val pageState = rememberPagerState()
    val onboardingPages = OnBoardingPageUiModel.values()
    SoptColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 69.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            pageCount = onboardingPages.size,
            state = pageState
        ) {
            OnboardingPage(
                image = onboardingPages[pageState.currentPage].image,
                title = onboardingPages[pageState.currentPage].title,
                content = onboardingPages[pageState.currentPage].content
            )
        }
        Spacer(modifier = Modifier.size(28.dp))
        PageIndicator(
            numberOfPages = onboardingPages.size,
            selectedPage = pageState.currentPage,
            defaultRadius = 4.dp,
            defaultColor = SoptTheme.colors.purple300,
            selectedColor = SoptTheme.colors.purple200,
            selectedLength = 20.dp,
            space = 10.dp,
            animationDurationInMillis = 100
        )
        Spacer(modifier = Modifier.size(52.dp))
        OnboardingButton(
            isButtonEnabled = (pageState.currentPage + 1 == onboardingPages.size),
            onClick = { navigator.popBackStack() }
        )
    }
}

@Composable
fun OnboardingButton(
    isButtonEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = { onClick() },
        enabled = isButtonEnabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SoptTheme.colors.purple300,
            disabledBackgroundColor = SoptTheme.colors.purple200
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = "확인",
            color = SoptTheme.colors.white,
            style = SoptTheme.typography.h2,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
