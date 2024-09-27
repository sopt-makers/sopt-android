package org.sopt.official.feature.fortune.fortuneDetail

import androidx.compose.foundation.layout.PaddingValues
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

internal class FortuneDetailScreenTest {

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
                    paddingValue = PaddingValues(),
                    date = date,
                    onFortuneAmuletClick = { },
                    onPokeClick = { },
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
