/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.fortune.fortuneDetail

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.feature.fortuneDetail.FortuneDetailScreen
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success.TodaySentence
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success.UserInfo

class FortuneDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun 서버통신이_성공하면_이름_솝마디_날짜가_노출된다() {
        // given:
        val date = "2024-09-26"
        val name = "이현우"
        val content = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요"

        // when:
        composeRule.setContent {
            SoptTheme {
                FortuneDetailScreen(
                    date = date,
                    onFortuneAmuletClick = { },
                    onPokeClick = { },
                    onProfileClick = { },
                    onErrorDialogCheckClick = { },
                    uiState = Success(
                        todaySentence = TodaySentence(
                            userName = name,
                            content = content,
                        ),
                        userInfo = UserInfo(
                            userId = 0L,
                            profile = "",
                            userName = "동민",
                            generation = 111,
                            part = "기획 파트",
                        )
                    ),
                    isEnabled = false,
                )
            }
        }

        // then:
        val todayFortune = composeRule.onNodeWithContentDescription("todaySentence")
            .fetchSemanticsNode().config.getOrNull(SemanticsProperties.Text)?.joinToString(separator = "").orEmpty()

        composeRule.waitForIdle()

        composeRule.onNodeWithText(date).assertIsDisplayed()
        assert(todayFortune.contains(name))
        assert(todayFortune.contains(content))
    }
}
