package org.sopt.official.feature.poke.bridge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.ApiResult
import org.sopt.official.domain.poke.entity.CheckNewInPoke
import org.sopt.official.domain.poke.usecase.CheckNewInPokeUseCase
import org.sopt.official.feature.poke.bridge.state.PokeEntryState
import javax.inject.Inject

@HiltViewModel
class PokeEntryViewModel @Inject constructor(
    private val checkNewInPokeUseCase: CheckNewInPokeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PokeEntryState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchIsNewPoke()
    }

    private fun fetchIsNewPoke() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val apiResult = checkNewInPokeUseCase()) {
                is ApiResult.Success<CheckNewInPoke> -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isNewPoke = apiResult.data.isNew,
                            // generation = apiResult.data.generation ?: 0
                        )
                    }
                }

                is ApiResult.ApiError, is ApiResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                }

            }
        }
    }

    fun updateToOldUser() {
        _uiState.update { it.copy(isNewPoke = false) }
    }
}