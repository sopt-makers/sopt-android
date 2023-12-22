package org.sopt.official.feature.poke.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.entity.CheckNewInPoke
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.use_case.CheckNewInPokeUseCase
import org.sopt.official.domain.poke.use_case.GetOnboardingPokeUserListUseCase
import org.sopt.official.domain.poke.use_case.PokeUserUseCase
import org.sopt.official.feature.poke.UiState
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val checkNewInPokeUseCase: CheckNewInPokeUseCase,
    private val getOnboardingPokeUserListUseCase: GetOnboardingPokeUserListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {

    private val _checkNewInPokeUiState = MutableStateFlow<UiState<CheckNewInPoke>>(UiState.Loading)
    val checkNewInPokeUiState: StateFlow<UiState<CheckNewInPoke>> get() = _checkNewInPokeUiState

    private val _onboardingPokeUserListUiState = MutableStateFlow<UiState<List<PokeUser>>>(UiState.Loading)
    val onboardingPokeUserListUiState: StateFlow<UiState<List<PokeUser>>> get() = _onboardingPokeUserListUiState

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState

    init {
        checkNewInPoke()
        getOnboardingPokeUserList()
    }

    fun checkNewInPoke() {
        viewModelScope.launch {
            checkNewInPokeUseCase.invoke()
                .onSuccess { response ->
                    _checkNewInPokeUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _checkNewInPokeUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _checkNewInPokeUiState.emit(UiState.Failure(throwable))
                }
        }
    }

    fun getOnboardingPokeUserList() {
        viewModelScope.launch {
            getOnboardingPokeUserListUseCase.invoke()
                .onSuccess { response ->
                    _onboardingPokeUserListUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _onboardingPokeUserListUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _onboardingPokeUserListUiState.emit(UiState.Failure(throwable))
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
