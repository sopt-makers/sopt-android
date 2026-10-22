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
package org.sopt.official.feature.poke.v2.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.domain.poke.usecase.GetPokeMessageListUseCase
import org.sopt.official.domain.poke.usecase.GetPokeNotificationListUseCase
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.PokeResultUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeMessageRowUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.toPokeResultUiState
import org.sopt.official.feature.poke.v2.model.PokeRelationChangeState
import timber.log.Timber

@HiltViewModel
class PokeNotificationViewModel @Inject constructor(
    private val getPokeNotificationListUseCase: GetPokeNotificationListUseCase,
    private val getPokeMessageListUseCase: GetPokeMessageListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<PokeNotificationUiState> = MutableStateFlow(PokeNotificationUiState())
    val uiState: StateFlow<PokeNotificationUiState>
        get() = _uiState.asStateFlow()

    private val _sideEffect: MutableSharedFlow<PokeNotificationSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<PokeNotificationSideEffect>
        get() = _sideEffect.asSharedFlow()

    private val _pokedUsersId: MutableStateFlow<List<Int>> = MutableStateFlow(emptyList())

    private var messageListJob: Job? = null

    val notifications = Pager(
        config = PagingConfig(pageSize = 25)
    ) {
        PokeNotificationPagingSource(getPokeNotificationListUseCase)
    }.flow
        .map { pagingData -> pagingData.map { it.toPokeMessageRowUiState() } }
        .cachedIn(viewModelScope)
        .combine(_pokedUsersId) { pagingData, pokedIds ->
            pagingData.map { notification ->
                if (notification.user.userId in pokedIds) {
                    notification.copy(
                        user = notification.user.copy(
                            isPokeButtonEnabled = false,
                            pokeCount = notification.user.pokeCount + 1
                        )
                    )
                } else {
                    notification
                }
            }
        }

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

        emitSideEffect(PokeNotificationSideEffect.ShowWarningSnackbar("익명 해제 시, 상대방이 나를 알 수 있어요."))
    }

    fun onRefresh() {
        _pokedUsersId.update { emptyList() }
        _uiState.update {
            it.copy(
                bottomSheetVisibility = false,
                pokeMessages = persistentListOf(),
                isAnonymous = true,
                target = null
            )
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
                    _sideEffect.emit(PokeNotificationSideEffect.ShowErrorSnackbar(message))
                }.onFailure { throwable ->
                    Timber.e(throwable)
                    closeBottomSheet()
                    _sideEffect.emit(PokeNotificationSideEffect.ShowErrorSnackbar("문제가 발생했어요"))
                }
        }
    }

    private fun pokeUser(userId: Int, isAnonymous: Boolean, message: String, isFirstMeet: Boolean) {
        viewModelScope.launch {
            pokeUserUseCase(userId = userId, isAnonymous = isAnonymous, message = message)
                .onSuccess { result ->
                    _pokedUsersId.update { it + userId }
                    closeBottomSheet()
                    handlePokeSuccess(result.toPokeResultUiState(requestedFirstMeet = isFirstMeet))
                }.onApiError { statusCode, message ->
                    Timber.e("statusCode: $statusCode, message: $message")
                    _sideEffect.emit(PokeNotificationSideEffect.ShowErrorSnackbar(message))
                }.onFailure { throwable ->
                    Timber.e(throwable)
                    _sideEffect.emit(PokeNotificationSideEffect.ShowErrorSnackbar("문제가 발생했어요"))
                }
        }
    }

    private suspend fun handlePokeSuccess(pokeResult: PokeResultUiState) {
        when {
            pokeResult.isPlainPoke -> _sideEffect.emit(PokeNotificationSideEffect.ShowSuccessSnackbar("콕 찌르기를 완료했어요."))
            pokeResult.becameFriend -> _sideEffect.emit(PokeNotificationSideEffect.ShowLottie(PokeRelationChangeState.Friend(pokeResult.user)))
            pokeResult.becameBestFriend -> _sideEffect.emit(PokeNotificationSideEffect.ShowLottie(PokeRelationChangeState.BestFriend(pokeResult.user)))
            pokeResult.becameSoulmate -> _sideEffect.emit(PokeNotificationSideEffect.ShowLottie(PokeRelationChangeState.Soulmate(pokeResult.user)))
        }
    }

    private fun emitSideEffect(sideEffect: PokeNotificationSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }
}
