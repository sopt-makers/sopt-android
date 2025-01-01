package org.sopt.official.feature.mypage.soptamp

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.sopt.official.domain.mypage.model.SoptampUser
import org.sopt.official.domain.mypage.repository.UserRepository
import org.sopt.official.feature.mypage.soptamp.state.ModifySoptampProfileUiState
import org.sopt.official.feature.mypage.soptamp.state.rememberModifyProfileState

@RunWith(RobolectricTestRunner::class)
class ModifyProfileStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeUserRepository = mockk<UserRepository>()

    @Test
    fun `should update profile message`() = runBlocking {
        val mockOnBackPressedDispatcherOwner = mockk<OnBackPressedDispatcherOwner>(relaxed = true)
        val mockSoftwareKeyboardController = mockk<SoftwareKeyboardController>(relaxed = true)
        var uiState: ModifySoptampProfileUiState? = null

        coEvery { fakeUserRepository.getUserInfo() } returns Result.success(
            SoptampUser(
                nickname = "nickname",
                points = 0,
                profileMessage = "profileMessage"
            )
        )
        coEvery { fakeUserRepository.updateProfileMessage(any()) } returns Result.success(Unit)

        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalOnBackPressedDispatcherOwner provides mockOnBackPressedDispatcherOwner,
                LocalSoftwareKeyboardController provides mockSoftwareKeyboardController
            ) {
                uiState = rememberModifyProfileState(
                    userRepository = fakeUserRepository,
                    onShowToast = { }
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.runOnIdle {
            uiState?.let {
                it.onChangeCurrent("newValue")
                it.onUpdate()
            }
        }

        verify { mockSoftwareKeyboardController.hide() }
    }
}