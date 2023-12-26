package org.sopt.official.feature.poke.message_bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.entity.PokeMessageList
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.domain.poke.use_case.GetPokeMessageListUseCase
import org.sopt.official.feature.poke.UiState
import javax.inject.Inject

@HiltViewModel
class MessageListBottomSheetViewModel @Inject constructor(
    private val getPokeMessageListUseCase: GetPokeMessageListUseCase,
) : ViewModel() {

    private val _pokeMessageListUiState = MutableStateFlow<UiState<PokeMessageList>>(UiState.Loading)
    val pokeMessageListUiState: StateFlow<UiState<PokeMessageList>> get() = _pokeMessageListUiState

    fun getPokeMessageList(pokeMessageType: PokeMessageType) {
        viewModelScope.launch {
            getPokeMessageListUseCase.invoke(
                messageType = pokeMessageType,
            )
                .onSuccess { response ->
                    _pokeMessageListUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _pokeMessageListUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _pokeMessageListUiState.emit(UiState.Failure(throwable))
                }
        }
    }
}