package org.sopt.official.feature.poke.friend_list_summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.entity.FriendListSummary
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.use_case.GetFriendListSummaryUseCase
import org.sopt.official.domain.poke.use_case.PokeUserUseCase
import org.sopt.official.feature.poke.UiState
import javax.inject.Inject

@HiltViewModel
class FriendListSummaryViewModel @Inject constructor(
    private val getFriendListSummaryUseCase: GetFriendListSummaryUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {

    private val _friendListSummaryUiState = MutableStateFlow<UiState<FriendListSummary>>(UiState.Loading)
    val friendListSummaryUiState: StateFlow<UiState<FriendListSummary>> get() = _friendListSummaryUiState

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState

    init {
        getFriendListSummary()
    }

    fun getFriendListSummary() {
        viewModelScope.launch {
            _friendListSummaryUiState.emit(UiState.Loading)
            getFriendListSummaryUseCase.invoke()
                .onSuccess { response ->
                    _friendListSummaryUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _friendListSummaryUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _friendListSummaryUiState.emit(UiState.Failure(throwable))
                }
        }
    }

    fun pokeUser(
        userId: Int,
        message: String,
    ) {
        viewModelScope.launch {
            _pokeUserUiState.emit(UiState.Loading)
            pokeUserUseCase.invoke(
                message = message,
                userId = userId,
            )
                .onSuccess { response ->
                    _pokeUserUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _pokeUserUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _pokeUserUiState.emit(UiState.Failure(throwable))
                }
        }
    }

    fun updatePokeUserState(userId: Int) {
        viewModelScope.launch {
            if (_friendListSummaryUiState.value is UiState.Success<FriendListSummary>) {
                val data = (_friendListSummaryUiState.value as UiState.Success<FriendListSummary>).data

                for (friend in data.newFriend) {
                    if (friend.userId == userId) {
                        friend.isAlreadyPoke = true
                    }
                }

                for (friend in data.bestFriend) {
                    if (friend.userId == userId) {
                        friend.isAlreadyPoke = true
                    }
                }

                for (friend in data.soulmate) {
                    if (friend.userId == userId) {
                        friend.isAlreadyPoke = true
                    }
                }
                _friendListSummaryUiState.emit(UiState.Loading)
                _friendListSummaryUiState.emit(UiState.Success(data))
            }
        }
    }
}