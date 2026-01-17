/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.usecase.GetOnboardingPokeUserListUseCase
import org.sopt.official.domain.poke.usecase.GetPokeFriendUseCase
import org.sopt.official.domain.poke.usecase.GetPokeMeUseCase
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.feature.poke.UiState
import timber.log.Timber

@Inject
@ViewModelKey(PokeMainViewModel::class)
@ContributesIntoMap(AppScope::class)
class PokeMainViewModel @Inject constructor(
    private val getPokeMeUseCase: GetPokeMeUseCase,
    private val getPokeFriendUseCase: GetPokeFriendUseCase,
    private val getOnboardingPokeUserListUseCase: GetOnboardingPokeUserListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {
    private val _pokeMeUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeMeUiState: StateFlow<UiState<PokeUser>> get() = _pokeMeUiState

    private val _pokeFriendUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeFriendUiState: StateFlow<UiState<PokeUser>> get() = _pokeFriendUiState

    private val _pokeSimilarFriendUiState = MutableStateFlow<UiState<List<PokeRandomUserList.PokeRandomUsers>>>(UiState.Loading)
    val pokeSimilarFriendUiState: StateFlow<UiState<List<PokeRandomUserList.PokeRandomUsers>>> get() = _pokeSimilarFriendUiState

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState

    private val _anonymousFriend = MutableStateFlow<PokeUser?>(null)
    val anonymousFriend: StateFlow<PokeUser?>
        get() = _anonymousFriend

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

    fun getPokeSimilarFriends() {
        viewModelScope.launch {
            _pokeSimilarFriendUiState.emit(UiState.Loading)
            getOnboardingPokeUserListUseCase.invoke(randomType = "ALL", size = 2)
                .onSuccess {
                    _pokeSimilarFriendUiState.emit(UiState.Success(it.randomInfoList))
                }
                .onApiError { statusCode, responseMessage ->
                    _pokeSimilarFriendUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure {
                    _pokeSimilarFriendUiState.emit(UiState.Failure(it))
                    Timber.e(it)
                }
        }
    }

    fun pokeUser(userId: Int, isAnonymous: Boolean, message: String, isFirstMeet: Boolean) {
        viewModelScope.launch {
            _pokeUserUiState.emit(UiState.Loading)
            pokeUserUseCase.invoke(
                userId = userId,
                isAnonymous = isAnonymous,
                message = message
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
                    _pokeMeUiState.emit(
                        UiState.Success(
                            oldData.copy(isAlreadyPoke = true),
                        ),
                    )
                }
            }
            if (_pokeFriendUiState.value is UiState.Success<PokeUser>) {
                val oldData = (_pokeFriendUiState.value as UiState.Success<PokeUser>).data
                if (oldData.userId == userId) {
                    _pokeFriendUiState.emit(UiState.Loading)
                    _pokeFriendUiState.emit(
                        UiState.Success(
                            oldData.copy(isAlreadyPoke = true),
                        ),
                    )
                }
            }
            if (_pokeSimilarFriendUiState.value is UiState.Success<List<PokeRandomUserList.PokeRandomUsers>>) {
                val oldData = (_pokeSimilarFriendUiState.value as UiState.Success<List<PokeRandomUserList.PokeRandomUsers>>).data
                for (friend in oldData) {
                    friend.userInfoList.find { it.userId == userId }?.isAlreadyPoke = true
                }
                _pokeSimilarFriendUiState.emit(UiState.Loading)
                _pokeSimilarFriendUiState.emit(UiState.Success(oldData))
            }
        }
    }

    fun setAnonymousFriend(pokeUser: PokeUser?) {
        _anonymousFriend.value = pokeUser
    }
}