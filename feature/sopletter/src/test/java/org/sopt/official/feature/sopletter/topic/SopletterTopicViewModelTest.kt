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
package org.sopt.official.feature.sopletter.topic

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.sopt.official.domain.sopletter.onboarding.model.SopletterOnboardingModel
import org.sopt.official.domain.sopletter.onboarding.repository.SopletterOnboardingRepository
import org.sopt.official.domain.sopletter.onboarding.usecase.SopletterProfileUseCase
import org.sopt.official.domain.sopletter.repository.SopletterRepository
import org.sopt.official.domain.sopletter.model.SopletterTopic
import retrofit2.HttpException
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

@OptIn(ExperimentalCoroutinesApi::class)
class SopletterTopicViewModelTest {
    private val sopletterRepository: SopletterRepository = mockk()
    private val onboardingRepository: SopletterOnboardingRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `topic 404 and missing profile show onboarding dialog instead of network error`() = runTest {
        coEvery { sopletterRepository.getTopics(null) } returns Result.failure(notFound())
        coEvery { onboardingRepository.getOnboarding() } returns Result.success(profile(isOnboarded = false))

        val viewModel = createViewModel()
        viewModel.fetchTopics().join()

        assertTrue(viewModel.uiState.value.isShowOnboardingDialog)
        assertFalse(viewModel.uiState.value.isShowErrorDialog)
        coVerify(exactly = 1) { onboardingRepository.getOnboarding() }
    }

    @Test
    fun `topic 404 and existing profile keep the network error dialog`() = runTest {
        coEvery { sopletterRepository.getTopics(null) } returns Result.failure(notFound())
        coEvery { onboardingRepository.getOnboarding() } returns Result.success(profile(isOnboarded = true))

        val viewModel = createViewModel()
        viewModel.fetchTopics().join()

        assertFalse(viewModel.uiState.value.isShowOnboardingDialog)
        assertTrue(viewModel.uiState.value.isShowErrorDialog)
        coVerify(exactly = 1) { onboardingRepository.getOnboarding() }
    }

    private fun createViewModel() = SopletterTopicViewModel(
        sopletterRepository = sopletterRepository,
        profileUseCase = SopletterProfileUseCase(onboardingRepository),
    )

    private fun profile(isOnboarded: Boolean) = SopletterOnboardingModel(
        nickname = "테스트",
        isOnboarded = isOnboarded,
        currentGeneration = 1,
    )

    private fun notFound(): HttpException = HttpException(
        Response.error<List<SopletterTopic>>(404, "not found".toResponseBody()),
    )
}
