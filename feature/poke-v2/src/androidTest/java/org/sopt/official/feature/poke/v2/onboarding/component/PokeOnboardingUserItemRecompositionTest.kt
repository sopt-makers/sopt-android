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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.mds.theme.SoptTheme
import java.util.concurrent.atomic.AtomicInteger

/**
 * 테스트 전용 카운터. 측정 대상 composable의 "파라미터"가 아니라 그냥 전역 참조로만 쓰여서,
 * [MeasuredSkipCandidate]/[MeasuredChangeCandidate] 의 안정성 판정(= skip 가능 여부)에 영향을 주지 않는다.
 */
private val skipCandidateRenderCount = AtomicInteger(0)
private val changeCandidateRenderCount = AtomicInteger(0)

/**
 * [PokeOnboardingUserItem] 과 완전히 동일한 파라미터 모양(stable user/onProfileClick/onPokeClick)을 가진
 * 최상위(top-level) 래퍼. 로컬(클로저 캡처) 함수가 아니라 top-level 함수여야 Compose의 통상적인
 * 파라미터 기반 skip 분석이 적용된다(최초 시도에서 로컬 함수로 만들었다가 이 차이 때문에 실패했었음).
 */
@Composable
private fun MeasuredSkipCandidate(
    user: PokeUserUiState,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
) {
    skipCandidateRenderCount.incrementAndGet()
    PokeOnboardingUserItem(user = user, onProfileClick = onProfileClick, onPokeClick = onPokeClick)
}

@Composable
private fun MeasuredChangeCandidate(
    user: PokeUserUiState,
    onProfileClick: (Int) -> Unit,
    onPokeClick: (PokeUserUiState) -> Unit,
) {
    changeCandidateRenderCount.incrementAndGet()
    PokeOnboardingUserItem(user = user, onProfileClick = onProfileClick, onPokeClick = onPokeClick)
}

/**
 * [PokeOnboardingUserItem] 이 실제 기기에서 Compose의 "donut-hole skipping" 대로 동작하는지 확인한다.
 *
 * Compose 컴파일러 metrics(`restartable skippable`)는 정적 추론일 뿐이라, 런타임에서
 * 실제로 스킵되는지를 직접 재구성 횟수를 세어 검증한다.
 */
class PokeOnboardingUserItemRecompositionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val stableUser = PokeUserUiState(
        userId = 1,
        userName = "테스트유저",
        userGeneration = 36,
        userPart = "안드로이드",
        profileImageUrl = null,
    )
    private val stableOnProfileClick: (Int) -> Unit = {}
    private val stableOnPokeClick: (PokeUserUiState) -> Unit = {}

    /** 본 테스트: 자신과 무관한 형제 state가 바뀌어도 재구성을 스킵해야 한다. */
    @Test
    fun skipsRecomposition_whenOnlyUnrelatedSiblingStateChanges() {
        skipCandidateRenderCount.set(0)

        composeTestRule.setContent {
            var unrelated by remember { mutableIntStateOf(0) }

            SoptTheme {
                Column {
                    Text(
                        text = "unrelated=$unrelated",
                        modifier = Modifier
                            .testTag(TRIGGER_TAG)
                            .clickable { unrelated++ },
                    )
                    MeasuredSkipCandidate(
                        user = stableUser,
                        onProfileClick = stableOnProfileClick,
                        onPokeClick = stableOnPokeClick,
                    )
                }
            }
        }

        repeat(3) {
            composeTestRule.onNodeWithTag(TRIGGER_TAG).performClick()
        }
        composeTestRule.waitForIdle()

        // user/onProfileClick/onPokeClick는 안 바뀌었으니 최초 1번만 그려져야 한다(= skip 성공).
        assertEquals(1, skipCandidateRenderCount.get())
    }

    /** 대조군: 자신의 user 파라미터가 실제로 바뀌면 매번 다시 그려져야 한다(= 측정 장치 자체가 유효함을 증명). */
    @Test
    fun recomposes_whenOwnUserParamActuallyChanges() {
        changeCandidateRenderCount.set(0)

        composeTestRule.setContent {
            var pokeCount by remember { mutableIntStateOf(0) }

            SoptTheme {
                Column {
                    Text(
                        text = "pokeCount=$pokeCount",
                        modifier = Modifier
                            .testTag(TRIGGER_TAG)
                            .clickable { pokeCount++ },
                    )
                    MeasuredChangeCandidate(
                        user = stableUser.copy(pokeCount = pokeCount),
                        onProfileClick = stableOnProfileClick,
                        onPokeClick = stableOnPokeClick,
                    )
                }
            }
        }

        repeat(3) {
            composeTestRule.onNodeWithTag(TRIGGER_TAG).performClick()
        }
        composeTestRule.waitForIdle()

        assertTrue(changeCandidateRenderCount.get() > 1)
    }

    private companion object {
        const val TRIGGER_TAG = "trigger"
    }
}
