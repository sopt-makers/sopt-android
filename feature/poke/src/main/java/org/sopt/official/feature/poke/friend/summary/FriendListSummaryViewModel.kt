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
package org.sopt.official.feature.poke.friend.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.poke.entity.FriendListSummary
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.usecase.GetFriendListSummaryUseCase
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.feature.poke.UiState
import org.sopt.official.feature.poke.navigation.PokeFriendList

@Inject
@ViewModelKey(FriendListSummaryViewModel::class)
@ContributesIntoMap(AppScope::class)
class FriendListSummaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFriendListSummaryUseCase: GetFriendListSummaryUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {
    val friendType = savedStateHandle.toRoute<PokeFriendList>()
    private val _friendListSummaryUiState = MutableStateFlow<UiState<FriendListSummary>>(UiState.Loading)
    val friendListSummaryUiState: StateFlow<UiState<FriendListSummary>> get() = _friendListSummaryUiState

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState

    private val _anonymousFriend = MutableStateFlow<PokeUser?>(null)
    val anonymousFriend: StateFlow<PokeUser?>
        get() = _anonymousFriend

    init {
        getFriendListSummary()
    }

    fun getFriendListSummary() {
        viewModelScope.launch {
            _friendListSummaryUiState.emit(UiState.Loading)
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

    fun pokeUser(userId: Int, isAnonymous: Boolean, message: String) {
        viewModelScope.launch {
            _pokeUserUiState.emit(UiState.Loading)
            pokeUserUseCase.invoke(
                userId = userId,
                isAnonymous = isAnonymous,
                message = message
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

    fun setAnonymousFriend(pokeUser: PokeUser?) {
        _anonymousFriend.value = pokeUser
    }
}