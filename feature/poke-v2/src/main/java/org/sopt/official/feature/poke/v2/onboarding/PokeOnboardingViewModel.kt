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
package org.sopt.official.feature.poke.v2.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.usecase.CheckNewInPokeOnboardingUseCase
import org.sopt.official.domain.poke.usecase.GetOnboardingPokeUserListUseCase
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.domain.poke.usecase.UpdateNewInPokeOnboardingUseCase
import org.sopt.official.feature.poke.v2.main.model.toPokeRecommendationUiStates
import org.sopt.official.feature.poke.v2.main.model.toPokeResultUiState
import org.sopt.official.feature.poke.v2.onboarding.navigation.PokeOnboarding
import org.sopt.official.feature.poke.v2.onboarding.model.PokeOnboardingSideEffect
import org.sopt.official.feature.poke.v2.onboarding.model.PokeOnboardingUiState
import javax.inject.Inject

@HiltViewModel
class PokeOnboardingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkNewInPokeOnboardingUseCase: CheckNewInPokeOnboardingUseCase,
    private val updateNewInPokeOnboardingUseCase: UpdateNewInPokeOnboardingUseCase,
    private val getOnboardingPokeUserListUseCase: GetOnboardingPokeUserListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {
    private val args: PokeOnboarding = savedStateHandle.toRoute<PokeOnboarding>()

    /** 딥링크(홈 배너)로 전달된 현재 기수. */
    val currentGeneration: Int get() = args.currentGeneration

    /** Amplitude view type 계산용. */
    val userStatus: String get() = args.userStatus

    private val _uiState = MutableStateFlow(PokeOnboardingUiState())
    val uiState: StateFlow<PokeOnboardingUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<PokeOnboardingSideEffect>()
    val sideEffect: SharedFlow<PokeOnboardingSideEffect> = _sideEffect.asSharedFlow()

    init {
        checkNewInPokeOnboarding()
        fetchOnboardingUsers()
    }

    /**
     * 온보딩을 처음 보는 유저인지 확인하고, 그렇다면 즉시 "본 것"으로 표시한 뒤
     * 안내 바텀시트를 띄우도록 상태를 갱신한다. (레거시 `checkNewInPokeOnboarding`)
     */
    fun checkNewInPokeOnboarding() {
        viewModelScope.launch {
            val isNew = checkNewInPokeOnboardingUseCase()
            if (isNew) {
                updateNewInPokeOnboardingUseCase()
            }
            _uiState.update { it.copy(shouldShowGuideBottomSheet = isNew) }
        }
    }

    /** 안내 바텀시트를 닫았을 때 호출. */
    fun dismissGuideBottomSheet() {
        _uiState.update { it.copy(shouldShowGuideBottomSheet = false) }
    }

    fun fetchOnboardingUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            getOnboardingPokeUserListUseCase(size = ONBOARDING_USER_LIST_SIZE)
                .onSuccess { list ->
                    _uiState.update {
                        it.copy(isLoading = false, sections = list.toPokeRecommendationUiStates())
                    }
                }
                .onApiError { _, _ -> _uiState.update { it.copy(isLoading = false, isError = true) } }
                .onFailure { _uiState.update { it.copy(isLoading = false, isError = true) } }
        }
    }

    /**
     * @param isFirstMeet 첫 만남(익명) 대상에게 보내는 콕인지. 성공 연출 판별에 사용.
     */
    fun pokeUser(userId: Int, isAnonymous: Boolean, message: String, isFirstMeet: Boolean) {
        viewModelScope.launch {
            pokeUserUseCase(userId = userId, isAnonymous = isAnonymous, message = message)
                .onSuccess { response ->
                    _uiState.update { it.markPoked(userId) }
                    _sideEffect.emit(
                        PokeOnboardingSideEffect.PokeCompleted(response.toPokeResultUiState(requestedFirstMeet = isFirstMeet)),
                    )
                }
                .onApiError { _, _ -> _sideEffect.emit(PokeOnboardingSideEffect.ShowError()) }
                .onFailure { throwable -> _sideEffect.emit(PokeOnboardingSideEffect.ShowError(throwable.message)) }
        }
    }

    private fun PokeOnboardingUiState.markPoked(userId: Int): PokeOnboardingUiState = copy(
        sections = sections.map { section ->
            section.copy(
                users = section.users.map { user ->
                    if (user.userId == userId) user.copy(isPokeButtonEnabled = false) else user
                }.toPersistentList(),
            )
        }.toPersistentList(),
    )

    private companion object {
        const val ONBOARDING_USER_LIST_SIZE = 6
    }
}
