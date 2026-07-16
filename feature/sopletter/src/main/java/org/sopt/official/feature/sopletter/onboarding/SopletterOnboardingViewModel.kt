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
package org.sopt.official.feature.sopletter.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.sopletter.onboarding.repository.SopletterOnboardingRepository
import org.sopt.official.feature.sopletter.onboarding.model.toUiModel
import org.sopt.official.localstorage.source.UserStorage
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SopletterOnboardingViewModel @Inject constructor(
    private val userStorage: UserStorage,
    private val sopletterOnboardingRepository: SopletterOnboardingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SopletterOnboardingState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<SopletterOnboardingSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchSopletterOnboarding()
    }

    fun fetchSopletterOnboarding(){
        viewModelScope.launch {
            sopletterOnboardingRepository.getOnboarding()
                .onSuccess {
                    userStorage.saveOnboardingCompleted(it.isOnboarded)
                    _state.update { currentState ->
                        currentState.copy(
                            onboardingUiModel = it.toUiModel()
                        )
                    }
                }
                .onFailure {
                    Timber.e(it)
                    _sideEffect.send(SopletterOnboardingSideEffect.ShowSnackbar("오류가 발생했어요. 다시 시도해주세요."))
                }
        }
    }

    fun updateSopletterOnboardingStatus() {
        viewModelScope.launch {
            sopletterOnboardingRepository.completeOnboarding()
                .onSuccess {
                    userStorage.saveOnboardingCompleted(it.isOnboarded)
                    _sideEffect.send(SopletterOnboardingSideEffect.NavigateToNickname(it.nickname, it.currentGeneration))
                }
                .onFailure {
                    Timber.e(it)
                    _sideEffect.send(SopletterOnboardingSideEffect.ShowSnackbar("오류가 발생했어요. 다시 시도해주세요."))
                }
        }
    }
}
