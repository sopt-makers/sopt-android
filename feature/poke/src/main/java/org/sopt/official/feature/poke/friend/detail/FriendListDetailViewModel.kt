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
package org.sopt.official.feature.poke.friend.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.poke.entity.PokeUser
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.usecase.GetFriendListDetailUseCase
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.feature.poke.UiState

@Inject
@ViewModelKey(FriendListDetailViewModel::class)
@ContributesIntoMap(AppScope::class)
class FriendListDetailViewModel @Inject constructor(
    private val getFriendListDetailUseCase: GetFriendListDetailUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {
    private val _friendListDetailUiState = MutableStateFlow<UiState<List<PokeUser>>>(UiState.Loading)
    val friendListDetailUiState: StateFlow<UiState<List<PokeUser>>> get() = _friendListDetailUiState

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState

    private val _anonymousFriend = MutableStateFlow<PokeUser?>(null)
    val anonymousFriend: StateFlow<PokeUser?>
        get() = _anonymousFriend

    private var totalPageSize = -1
    private var currentPaginationIndex = 0
    private var friendListJob: Job? = null

    fun getFriendListDetail(pokeFriendType: PokeFriendType) {
        friendListJob?.let {
            if (it.isActive || !it.isCompleted) return
        }

        if (currentPaginationIndex == totalPageSize) return

        friendListJob =
            viewModelScope.launch {
                val oldData =
                    when (_friendListDetailUiState.value is UiState.Success) {
                        true -> (_friendListDetailUiState.value as UiState.Success<List<PokeUser>>).data
                        false -> emptyList()
                    }
                _friendListDetailUiState.emit(UiState.Loading)
                getFriendListDetailUseCase.invoke(
                    page = currentPaginationIndex,
                    type = pokeFriendType,
                )
                    .onSuccess { response ->
                        totalPageSize = response.totalPageSize
                        currentPaginationIndex = response.pageNum
                        _friendListDetailUiState.emit(UiState.Success(oldData.plus(response.friendList)))
                    }
                    .onApiError { statusCode, responseMessage ->
                        _friendListDetailUiState.emit(UiState.ApiError(statusCode, responseMessage))
                    }
                    .onFailure { throwable ->
                        _friendListDetailUiState.emit(UiState.Failure(throwable))
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
                    updatePokeUserState(response.userId)
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

    private suspend fun updatePokeUserState(userId: Int) {
        viewModelScope.launch {
            if (_friendListDetailUiState.value is UiState.Success<List<PokeUser>>) {
                val newList =
                    (_friendListDetailUiState.value as UiState.Success<List<PokeUser>>).data
                        .map { pokeUser ->
                            when (pokeUser.userId != userId) {
                                true -> pokeUser
                                false ->
                                    pokeUser.copy(
                                        pokeNum = pokeUser.pokeNum + 1,
                                        isAlreadyPoke = true,
                                    )
                            }
                        }
                _friendListDetailUiState.emit(UiState.Loading)
                launch {
                    _friendListDetailUiState.emit(UiState.Success(newList))
                }
            }
        }
    }

    fun setAnonymousFriend(pokeUser: PokeUser?) {
        _anonymousFriend.value = pokeUser
    }
}