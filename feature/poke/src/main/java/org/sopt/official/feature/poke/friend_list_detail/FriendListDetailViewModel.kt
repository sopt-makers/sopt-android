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
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.use_case.GetFriendListDetailUseCase
import org.sopt.official.feature.poke.UiState
import javax.inject.Inject

@HiltViewModel
class FriendListDetailViewModel @Inject constructor(
    private val getFriendListDetailUseCase: GetFriendListDetailUseCase,
) : ViewModel() {

    private val _friendListDetailUiState = MutableStateFlow<UiState<FriendListDetail>>(UiState.Loading)
    val friendListDetailUiState: StateFlow<UiState<FriendListDetail>> get() = _friendListDetailUiState


    init {
        getFriendListDetail()
    }

    fun getFriendListDetail() {
        viewModelScope.launch {
            getFriendListDetailUseCase.invoke(
                page = 0,
                type = PokeFriendType.NEW,
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
}