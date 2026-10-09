/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.v2.onboarding.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.theme.SoptTheme
import java.util.concurrent.atomic.AtomicInteger

/** 측정용 전역 카운터. [MeasuredOnboardingPage]의 파라미터가 아니라 순수 참조라 안정성 판정에 영향 없음. */
private val pageRenderCount = AtomicInteger(0)

/** [PokeOnboardingScreen] 내부 `HorizontalPager` 콘텐츠와 동일한 구조(페이지당 2열 유저 그리드). */
@Composable
private fun MeasuredOnboardingPage(users: List<PokeUserUiState>) {
    pageRenderCount.incrementAndGet()
    Column {
        users.chunked(2).forEach { rowUsers ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowUsers.forEach { user ->
                    PokeOnboardingUserItem(
                        user = user,
                        onProfileClick = {},
                        onPokeClick = {},
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}


class PokeOnboardingPagerSwipeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val pages: List<List<PokeUserUiState>> = (0 until 3).map { pageIndex ->
        (0 until 2).map { userIndex ->
            PokeUserUiState(
                userId = pageIndex * 10 + userIndex,
                userName = "유저$pageIndex-$userIndex",
                userGeneration = 36,
                userPart = "안드로이드",
                profileImageUrl = null,
            )
        }
    }

    @Test
    fun pageContentIsNotRecomposedPerFrame_duringSwipeSettleAnimation() {
        pageRenderCount.set(0)
        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.setContent {
            SoptTheme {
                val pagerState = rememberPagerState(pageCount = { pages.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.testTag(PAGER_TAG),
                ) { pageIndex ->
                    MeasuredOnboardingPage(users = pages[pageIndex])
                }
            }
        }

        composeTestRule.mainClock.advanceTimeByFrame()
        composeTestRule.waitForIdle()
        val renderCountBeforeSwipe = pageRenderCount.get()

        composeTestRule.onNodeWithTag(PAGER_TAG).performTouchInput { swipeLeft() }

        // settle 애니메이션 동안 프레임을 하나씩 직접 흘려본다(최대 1초치).
        repeat(MAX_FRAMES_TO_SETTLE) {
            composeTestRule.mainClock.advanceTimeByFrame()
        }
        composeTestRule.waitForIdle()
        val renderCountAfterSwipe = pageRenderCount.get()

        val newRenders = renderCountAfterSwipe - renderCountBeforeSwipe
        assertTrue(
            "스와이프 settle 동안 페이지 콘텐츠가 ${newRenders}번 재구성됨(흘려보낸 프레임 수: $MAX_FRAMES_TO_SETTLE) " +
                "— 새로 보이는 페이지 1개가 구성되는 수준을 넘어 프레임마다 재구성되고 있다면 jank 유발 가능성이 있음",
            newRenders <= MAX_NEW_PAGE_RENDERS,
        )
    }

    private companion object {
        const val PAGER_TAG = "pager"
        const val MAX_FRAMES_TO_SETTLE = 60
        const val MAX_NEW_PAGE_RENDERS = 3
    }
}
