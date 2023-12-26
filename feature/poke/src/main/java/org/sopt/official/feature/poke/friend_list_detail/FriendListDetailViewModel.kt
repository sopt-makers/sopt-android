package org.sopt.official.feature.poke.friend_list_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.entity.FriendListDetail
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.use_case.GetFriendListDetailUseCase
import org.sopt.official.domain.poke.use_case.PokeUserUseCase
import org.sopt.official.feature.poke.UiState
import javax.inject.Inject

@HiltViewModel
class FriendListDetailViewModel @Inject constructor(
    private val getFriendListDetailUseCase: GetFriendListDetailUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {

    private val _friendListDetailUiState = MutableStateFlow<UiState<FriendListDetail>>(UiState.Loading)
    val friendListDetailUiState: StateFlow<UiState<FriendListDetail>> get() = _friendListDetailUiState

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState

    fun getFriendListDetail(pokeFriendType: PokeFriendType) {
        viewModelScope.launch {
            getFriendListDetailUseCase.invoke(
                page = 0,
                type = pokeFriendType,
            )
                .onSuccess { response ->
                    _friendListDetailUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _friendListDetailUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _friendListDetailUiState.emit(UiState.Failure(throwable))
                }
        }
    }

    fun pokeUser(
        userId: Int,
        message: String,
    ) {
        viewModelScope.launch {
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
}