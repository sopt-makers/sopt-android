package org.sopt.official.feature.poke.v2.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import org.sopt.official.domain.poke.usecase.GetFriendListSummaryUseCase
import org.sopt.official.feature.poke.v2.component.PokeSnackBarType
import org.sopt.official.feature.poke.v2.friend.model.toPokeFriendListSections
import javax.inject.Inject

@HiltViewModel
class PokeFriendViewModel @Inject constructor(
    private val getFriendListSummaryUseCase: GetFriendListSummaryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokeFriendState())
    val uiState: StateFlow<PokeFriendState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<PokeFriendSideEffect>()
    val sideEffect: SharedFlow<PokeFriendSideEffect> = _sideEffect.asSharedFlow()

    init {
        getFriendListSummary()
    }
    fun getFriendListSummary() {
        viewModelScope.launch {
            getFriendListSummaryUseCase()
                .onSuccess { summary ->
                    _uiState.update { it.copy(sections = summary.toPokeFriendListSections()) }
                }
                .onApiError { _, _ ->
                    emitErrorSnackbar()
                }
                .onFailure { throwable ->
                    emitErrorSnackbar(throwable)
                }
        }
    }

    private suspend fun emitErrorSnackbar(throwable: Throwable? = null) {
        _sideEffect.emit(
            PokeFriendSideEffect.ShowSnackbar(
                message = throwable?.message ?: "문제가 발생했습니다.",
                type = PokeSnackBarType.FAILURE,
            ),
        )
    }
}
