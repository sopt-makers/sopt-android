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
import org.sopt.official.domain.poke.use_case.GetFriendListSummaryUseCase
import org.sopt.official.feature.poke.UiState
import javax.inject.Inject

@HiltViewModel
class FriendListSummaryViewModel @Inject constructor(
    private val getFriendListSummaryUseCase: GetFriendListSummaryUseCase,
) : ViewModel() {

    private val _friendListSummaryUiState = MutableStateFlow<UiState<FriendListSummary>>(UiState.Loading)
    val friendListSummaryUiState: StateFlow<UiState<FriendListSummary>> get() = _friendListSummaryUiState

    init {
        getFriendListSummary()
    }

    fun getFriendListSummary() {
        viewModelScope.launch {
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
}