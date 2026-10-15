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
package org.sopt.official.feature.poke.v2.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.domain.poke.usecase.GetOnboardingPokeUserListUseCase
import org.sopt.official.domain.poke.usecase.GetPokeFriendUseCase
import org.sopt.official.domain.poke.usecase.GetPokeMeUseCase
import org.sopt.official.domain.poke.usecase.GetPokeMessageListUseCase
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.feature.poke.v2.model.PokeRelationChangeState
import org.sopt.official.feature.poke.v2.main.model.PokeMainSideEffect
import org.sopt.official.feature.poke.v2.main.model.PokeMainUiState
import org.sopt.official.feature.poke.v2.main.model.PokeMessageRowUiState
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.PokeResultUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeMessageRowUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeRecommendationUiStates
import org.sopt.official.feature.poke.v2.main.model.toPokeResultUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeUserUiState
import timber.log.Timber

/**
 * 콕 찌르기 메인 화면 ViewModel.
 *
 * - 상태는 단일 [PokeMainUiState] 로 통합
 * - 콕 요청 결과 등 일회성 이벤트는 [PokeMainSideEffect] 로 분리
 * - 도메인 엔티티(`PokeUser`) → UI 모델(`PokeUserUiState`) 변환을 ViewModel 경계에서 수행
 */
@HiltViewModel
class PokeMainViewModel @Inject constructor(
    private val getPokeMeUseCase: GetPokeMeUseCase,
    private val getPokeFriendUseCase: GetPokeFriendUseCase,
    private val getOnboardingPokeUserListUseCase: GetOnboardingPokeUserListUseCase,
    private val getPokeMessageListUseCase: GetPokeMessageListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PokeMainUiState())
    val uiState: StateFlow<PokeMainUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<PokeMainSideEffect>()
    val sideEffect: SharedFlow<PokeMainSideEffect> = _sideEffect.asSharedFlow()

    private var messageListJob: Job? = null

    init {
        load()
    }

    fun refresh() = load(isRefresh = true)

    private fun load(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = !isRefresh, isRefreshing = isRefresh) }
            joinAll(
                launch { fetchPokeMe() },
                launch { fetchFriend() },
                launch { fetchRecommendations() },
            )
            _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
        }
    }

    private suspend fun fetchPokeMe() {
        getPokeMeUseCase()
            .onSuccess { user -> _uiState.update { it.copy(pokeMe = user.toPokeMessageRowUiState()) } }
            .onApiError { _, _ -> _uiState.update { it.copy(pokeMe = null) } }
            .onFailure { _uiState.update { it.copy(pokeMe = null) } }
    }

    private suspend fun fetchFriend() {
        getPokeFriendUseCase()
            .onSuccess { friends ->
                _uiState.update { it.copy(friend = friends.firstOrNull()?.toPokeUserUiState()) }
            }
            // 서버가 명시적으로 에러를 내려준 경우만 실패로 취급한다. (친구가 없는 건 정상 → 카드만 숨김)
            .onApiError { statusCode, message ->
                Timber.e("statusCode: $statusCode, message: $message")
                _uiState.update { it.copy(friend = null) }
                _sideEffect.emit(PokeMainSideEffect.ShowError(message))
            }.onFailure { throwable ->
                Timber.e(throwable)
                _uiState.update { it.copy(friend = null) }
                _sideEffect.emit(PokeMainSideEffect.ShowError("문제가 발생했어요"))
            }
    }

    private suspend fun fetchRecommendations() {
        getOnboardingPokeUserListUseCase(randomType = "ALL", size = 2)
            .onSuccess { list ->
                _uiState.update { it.copy(recommendations = list.toPokeRecommendationUiStates()) }
            }.onApiError { statusCode, message ->
                Timber.e("statusCode: $statusCode, message: $message")
            }.onFailure { throwable ->
                _sideEffect.emit(PokeMainSideEffect.ShowError("문제가 발생했어요"))
            }
    }

    /**
     * @param isFirstMeet 첫 만남(익명) 대상에게 보내는 콕인지. 성공 연출 판별에 사용.
     */
    private fun pokeUser(userId: Int, isAnonymous: Boolean, message: String, isFirstMeet: Boolean) {
        viewModelScope.launch {
            pokeUserUseCase(userId = userId, isAnonymous = isAnonymous, message = message)
                .onSuccess { response ->
                    _uiState.update { it.markPoked(userId) }
                    closeBottomSheet()
                    handlePokeSuccess(response.toPokeResultUiState(requestedFirstMeet = isFirstMeet))
                }
                .onApiError { statusCode, message ->
                    Timber.e("statusCode: $statusCode, message: $message")
                    _sideEffect.emit(PokeMainSideEffect.ShowError(message))
                }.onFailure { throwable ->
                    _sideEffect.emit(PokeMainSideEffect.ShowError("문제가 발생했어요"))
                }
        }
    }


    private suspend fun handlePokeSuccess(pokeResult: PokeResultUiState) {
        when {
            pokeResult.isPlainPoke -> _sideEffect.emit(PokeMainSideEffect.ShowSuccess("콕 찌르기를 완료했어요."))
            pokeResult.becameFriend -> _sideEffect.emit(PokeMainSideEffect.PokeCompleted(PokeRelationChangeState.Friend(pokeResult.user)))
            pokeResult.becameBestFriend -> _sideEffect.emit(PokeMainSideEffect.PokeCompleted(PokeRelationChangeState.BestFriend(pokeResult.user)))
            pokeResult.becameSoulmate -> _sideEffect.emit(PokeMainSideEffect.PokeCompleted(PokeRelationChangeState.Soulmate(pokeResult.user)))
        }
    }

    /** 콕을 보낸 유저의 버튼을 상태 전반에서 비활성화한다. (레거시 `updatePokeUserState`) */
    private fun PokeMainUiState.markPoked(userId: Int): PokeMainUiState = copy(
        pokeMe = pokeMe?.disablePokeIfMatch(userId),
        friend = friend?.disablePokeIfMatch(userId),
        recommendations = recommendations.map { section ->
            section.copy(users = section.users.map { it.disablePokeIfMatch(userId) }.toPersistentList())
        }.toPersistentList(),
    )

    private fun PokeUserUiState.disablePokeIfMatch(userId: Int): PokeUserUiState =
        if (this.userId == userId) copy(isPokeButtonEnabled = false) else this

    private fun PokeMessageRowUiState.disablePokeIfMatch(userId: Int): PokeMessageRowUiState =
        if (this.user.userId == userId) copy(user = user.copy(isPokeButtonEnabled = false)) else this


    fun onPokeButtonClick(target: PokeUserUiState) {
        openBottomSheet(target)
    }

    fun onMessageClick(message: PokeMessageUiState) {
        val target = _uiState.value.target ?: return

        pokeUser(
            userId = target.userId,
            isAnonymous = _uiState.value.isAnonymous,
            message = message.content,
            isFirstMeet = target.isFirstMeet
        )
    }

    fun closeBottomSheet() {
        messageListJob?.cancel()

        _uiState.update {
            it.copy(bottomSheetVisibility = false)
        }
    }

    fun toggleAnonymous() {
        _uiState.update {
            it.copy(isAnonymous = !it.isAnonymous)
        }

        viewModelScope.launch {
            _sideEffect.emit(PokeMainSideEffect.ShowWarning("익명 해제 시, 상대방이 나를 알 수 있어요."))
        }
    }


    private fun openBottomSheet(target: PokeUserUiState) {
        _uiState.update {
            it.copy(
                bottomSheetVisibility = true,
                target = target
            )
        }

        val messageType = if (target.isFirstMeet) PokeMessageType.REPLY_NEW else PokeMessageType.POKE_FRIEND

        getMessageList(messageType)
    }

    private fun getMessageList(messageType: PokeMessageType) {
        messageListJob?.cancel()

        messageListJob = viewModelScope.launch {
            getPokeMessageListUseCase(messageType = messageType)
                .onSuccess { messageList ->
                    _uiState.update { state ->
                        state.copy(
                            header = messageList.header,
                            pokeMessages = messageList.messages
                                .map { it.toPokeMessageUiState() }
                                .toImmutableList()
                        )
                    }
                }.onApiError { statusCode, message ->
                    Timber.e("statusCode: $statusCode, message: $message")
                    closeBottomSheet()
                    _sideEffect.emit(PokeMainSideEffect.ShowSuccess(message))
                }.onFailure { throwable ->
                    Timber.e(throwable)
                    closeBottomSheet()
                    _sideEffect.emit(PokeMainSideEffect.ShowError("문제가 발생했어요"))
                }
        }
    }
}
