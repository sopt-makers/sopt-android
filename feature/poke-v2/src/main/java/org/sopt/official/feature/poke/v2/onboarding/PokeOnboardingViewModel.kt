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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.domain.poke.usecase.CheckNewInPokeOnboardingUseCase
import org.sopt.official.domain.poke.usecase.GetOnboardingPokeUserListUseCase
import org.sopt.official.domain.poke.usecase.GetPokeMessageListUseCase
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.domain.poke.usecase.UpdateNewInPokeOnboardingUseCase
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeRecommendationUiStates
import org.sopt.official.feature.poke.v2.main.model.toPokeResultUiState
import org.sopt.official.feature.poke.v2.onboarding.model.PokeOnboardingSideEffect
import org.sopt.official.feature.poke.v2.onboarding.model.PokeOnboardingUiState
import org.sopt.official.feature.poke.v2.onboarding.navigation.PokeOnboarding
import javax.inject.Inject

@HiltViewModel
class PokeOnboardingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkNewInPokeOnboardingUseCase: CheckNewInPokeOnboardingUseCase,
    private val updateNewInPokeOnboardingUseCase: UpdateNewInPokeOnboardingUseCase,
    private val getOnboardingPokeUserListUseCase: GetOnboardingPokeUserListUseCase,
    private val getPokeMessageListUseCase: GetPokeMessageListUseCase,
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

    /** 진행 중인 콕 찌르기 요청. 완료 전 중복 호출을 막기 위해 보관한다. */
    private var pokeJob: Job? = null

    init {
        checkNewInPokeOnboarding()
        fetchOnboardingUsers()
    }

    /**
     * 온보딩을 처음 보는 유저인지 확인하고, 그렇다면 즉시 "본 것"으로 표시한 뒤
     * 안내 바텀시트를 띄우도록 상태를 갱신한다.
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

    fun fetchOnboardingUsers(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = !isRefresh,
                    isRefreshing = isRefresh,
                    isError = false
                )
            }

            getOnboardingPokeUserListUseCase(size = ONBOARDING_USER_LIST_SIZE)
                .onSuccess { list ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            pages = list.toPokeRecommendationUiStates(),
                        )
                    }
                }
                .onApiError { _, _ ->
                    _uiState.update {
                        it.copy(isLoading = false, isRefreshing = false, isError = true)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(isLoading = false, isRefreshing = false, isError = true)
                    }
                }
        }
    }

    /** 유저의 콕 찌르기 버튼 탭 → 메시지 바텀시트를 띄우고 메시지 목록을 불러온다. */
    fun openPokeMessageSheet(user: PokeUserUiState) {
        _uiState.update {
            it.copy(
                pokeTarget = user,
                isAnonymous = true,
                messages = persistentListOf()
            )
        }
        viewModelScope.launch {
            getPokeMessageListUseCase(PokeMessageType.POKE_SOMEONE)
                .onSuccess { list ->
                    _uiState.update {
                        it.copy(
                            messages = list.messages.map { message -> message.toPokeMessageUiState() }.toPersistentList()
                        )
                    }
                }
                .onApiError { _, _ ->
                    _sideEffect.emit(PokeOnboardingSideEffect.ShowError())
                }
                .onFailure { throwable ->
                    _sideEffect.emit(PokeOnboardingSideEffect.ShowError(throwable.message))
                }
        }
    }

    /** 메시지 바텀시트를 닫는다. */
    fun dismissPokeMessageSheet() {
        _uiState.update {
            it.copy(pokeTarget = null, messages = persistentListOf())
        }
    }

    /** 메시지 바텀시트의 익명 체크박스 토글. */
    fun toggleAnonymous() {
        _uiState.update {
            it.copy(isAnonymous = !it.isAnonymous)
        }
    }

    /** 메시지를 선택해 [PokeOnboardingUiState.pokeTarget] 에게 콕을 보낸다. */
    fun pokeUser(message: PokeMessageUiState) {
        if (pokeJob?.isActive == true) return
        val target = _uiState.value.pokeTarget ?: return
        val isAnonymous = _uiState.value.isAnonymous && !target.isAnonymousCheckboxLocked

        pokeJob = viewModelScope.launch {
            pokeUserUseCase(userId = target.userId, isAnonymous = isAnonymous, message = message.content)
                .onSuccess { response ->
                    _uiState.update {
                        it.markPoked(target.userId).copy(
                            pokeTarget = null, messages = persistentListOf()
                        )
                    }
                    _sideEffect.emit(
                        PokeOnboardingSideEffect.PokeCompleted(
                            response.toPokeResultUiState(requestedFirstMeet = target.isFirstMeet),
                        ),
                    )
                }
                .onApiError { _, _ ->
                    dismissPokeMessageSheet()
                    _sideEffect.emit(PokeOnboardingSideEffect.ShowError())
                }
                .onFailure { throwable ->
                    dismissPokeMessageSheet()
                    _sideEffect.emit(PokeOnboardingSideEffect.ShowError(throwable.message))
                }
        }
    }

    private fun PokeOnboardingUiState.markPoked(userId: Int): PokeOnboardingUiState = copy(
        pages = pages.map { page ->
            page.copy(
                users = page.users.map { user ->
                    if (user.userId == userId) user.copy(isPokeButtonEnabled = false) else user
                }.toPersistentList(),
            )
        }.toPersistentList(),
    )

    private companion object {
        const val ONBOARDING_USER_LIST_SIZE = 6
    }
}
