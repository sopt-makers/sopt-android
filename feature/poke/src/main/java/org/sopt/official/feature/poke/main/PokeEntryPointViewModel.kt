/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
class PokeEntryPointViewModel @Inject constructor(
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
            _pokeMeUiState.emit(UiState.Loading)
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
            _pokeFriendUiState.emit(UiState.Loading)
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
            _pokeFriendOfFriendUiState.emit(UiState.Loading)
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
        isFirstMeet: Boolean,
    ) {
        viewModelScope.launch {
            _pokeUserUiState.emit(UiState.Loading)
            pokeUserUseCase.invoke(
                message = message,
                userId = userId,
            )
                .onSuccess { response ->
                    _pokeUserUiState.emit(UiState.Success(response, isFirstMeet))
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
            if (_pokeMeUiState.value is UiState.Success<PokeUser>) {
                val oldData = (_pokeMeUiState.value as UiState.Success<PokeUser>).data
                if (oldData.userId == userId) {
                    _pokeMeUiState.emit(UiState.Loading)
                    _pokeMeUiState.emit(UiState.Success(
                        oldData.copy(isAlreadyPoke = true)
                    ))
                }
            }
            if (_pokeFriendUiState.value is UiState.Success<PokeUser>) {
                val oldData = (_pokeFriendUiState.value as UiState.Success<PokeUser>).data
                if (oldData.userId == userId) {
                    _pokeFriendUiState.emit(UiState.Loading)
                    _pokeFriendUiState.emit(UiState.Success(
                        oldData.copy(isAlreadyPoke = true)
                    ))
                }
            }
            if (_pokeFriendOfFriendUiState.value is UiState.Success<List<PokeFriendOfFriendList>>) {
                val oldData = (_pokeFriendOfFriendUiState.value as UiState.Success<List<PokeFriendOfFriendList>>).data
                for (friend in oldData) {
                    friend.friendList.find { it.userId == userId }?.isAlreadyPoke = true
                }
                _pokeFriendOfFriendUiState.emit(UiState.Loading)
                _pokeFriendOfFriendUiState.emit(UiState.Success(oldData))
            }
        }
    }
}
