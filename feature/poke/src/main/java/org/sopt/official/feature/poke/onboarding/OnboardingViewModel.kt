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
package org.sopt.official.feature.poke.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.usecase.CheckNewInPokeOnboardingUseCase
import org.sopt.official.domain.poke.usecase.GetOnboardingPokeUserListUseCase
import org.sopt.official.domain.poke.usecase.UpdateNewInPokeOnboardingUseCase
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.navigation.PokeOnboarding

@Inject
@ViewModelKey(OnboardingViewModel::class)
@ContributesIntoMap(AppScope::class)
class OnboardingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkNewInPokeOnboardingUseCase: CheckNewInPokeOnboardingUseCase,
    private val updateNewInPokeOnboardingUseCase: UpdateNewInPokeOnboardingUseCase,
    private val getOnboardingPokeUserListUseCase: GetOnboardingPokeUserListUseCase,
) : ViewModel() {
    val args = savedStateHandle.toRoute<PokeOnboarding>()

    private val _checkNewInPokeOnboardingState = MutableStateFlow<Boolean?>(null)
    val checkNewInPokeOnboardingState: StateFlow<Boolean?> get() = _checkNewInPokeOnboardingState

    private val _onboardingPokeUserListUiState = MutableStateFlow<UiState<PokeRandomUserList>>(UiState.Loading)
    val onboardingPokeUserListUiState: StateFlow<UiState<PokeRandomUserList>> get() = _onboardingPokeUserListUiState

    init {
        checkNewInPokeOnboarding()
        getOnboardingPokeUserList()
    }

    private fun checkNewInPokeOnboarding() {
        viewModelScope.launch {
            _checkNewInPokeOnboardingState.emit(checkNewInPokeOnboardingUseCase.invoke())
        }
    }

    fun updateNewInPokeOnboarding() {
        viewModelScope.launch {
            updateNewInPokeOnboardingUseCase.invoke()
        }
    }

    private fun getOnboardingPokeUserList() {
        viewModelScope.launch {
            _onboardingPokeUserListUiState.emit(UiState.Loading)
            getOnboardingPokeUserListUseCase.invoke(size = 6)
                .onSuccess { response ->
                    _onboardingPokeUserListUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _onboardingPokeUserListUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _onboardingPokeUserListUiState.emit(UiState.Failure(throwable))
                }
        }
    }
}