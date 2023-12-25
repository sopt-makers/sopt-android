package org.sopt.official.feature.poke.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.poke.entity.PokeFriendOfFriendList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.use_case.GetPokeFriendOfFriendListUseCase
import org.sopt.official.domain.poke.use_case.GetPokeFriendUseCase
import org.sopt.official.domain.poke.use_case.GetPokeMeUseCase
import org.sopt.official.domain.poke.use_case.PokeUserUseCase
import org.sopt.official.feature.poke.UiState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokeMainViewModel @Inject constructor(
    private val getPokeMeUseCase: GetPokeMeUseCase,
    private val getPokeFriendUseCase: GetPokeFriendUseCase,
    private val getPokeFriendOfFriendListUseCase: GetPokeFriendOfFriendListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {

    private val _pokeMeUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeMeUiState: StateFlow<UiState<PokeUser>> get() = _pokeMeUiState

    private val _pokeFriendUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeFriendUiState: StateFlow<UiState<PokeUser>> get() = _pokeFriendUiState

    private val _pokeFriendOfFriendUiState = MutableStateFlow<UiState<List<PokeFriendOfFriendList>>>(UiState.Loading)
    val pokeFriendOfFriendUiState: StateFlow<UiState<List<PokeFriendOfFriendList>>> get() = _pokeFriendOfFriendUiState

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState

    fun getPokeMe() {
        viewModelScope.launch {
            getPokeMeUseCase.invoke()
                .onSuccess { // todo: 데이터 없으면 null로 넘어옴.. 처리 필요..
                    _pokeMeUiState.emit(UiState.Success(it))
                }
                .onApiError { statusCode, responseMessage ->
                    _pokeMeUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure {
                    _pokeMeUiState.emit(UiState.Failure(it))
                    Timber.e(it)
                }
        }
    }

    fun getPokeFriend() {
        viewModelScope.launch {
            getPokeFriendUseCase.invoke()
                .onSuccess {
                    _pokeFriendUiState.emit(UiState.Success(it[0]))
                }
                .onApiError { statusCode, responseMessage ->
                    _pokeFriendUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure {
                    _pokeFriendUiState.emit(UiState.Failure(it))
                    Timber.e(it)
                }
        }
    }

    fun getPokeFriendOfFriend() {
        viewModelScope.launch {
            getPokeFriendOfFriendListUseCase.invoke()
                .onSuccess {
                    _pokeFriendOfFriendUiState.emit(UiState.Success(it))
                }
                .onApiError { statusCode, responseMessage ->
                    _pokeFriendOfFriendUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure {
                    _pokeFriendOfFriendUiState.emit(UiState.Failure(it))
                    Timber.e(it)
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