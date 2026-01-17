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
package org.sopt.official.feature.poke.notification

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
import org.sopt.official.domain.poke.usecase.GetPokeNotificationListUseCase
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.feature.poke.UiState

@Inject
@ViewModelKey(PokeNotificationViewModel::class)
@ContributesIntoMap(AppScope::class)
class PokeNotificationViewModel @Inject constructor(
    private val getPokeNotificationListUseCase: GetPokeNotificationListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {
    private val _pokeNotification = MutableStateFlow<UiState<List<PokeUser>>>(UiState.Loading)
    val pokeNotification: StateFlow<UiState<List<PokeUser>>> get() = _pokeNotification

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState

    private val _anonymousFriend = MutableStateFlow<PokeUser?>(null)
    val anonymousFriend: StateFlow<PokeUser?>
        get() = _anonymousFriend

    private var totalPageSize = -1
    private var currentPaginationIndex = 0
    private var pokeNotificationJob: Job? = null

    init {
        getPokeNotification()
    }

    fun getPokeNotification() {
        pokeNotificationJob?.let {
            if (it.isActive || !it.isCompleted) return
        }

        if (currentPaginationIndex == totalPageSize - 1) return

        pokeNotificationJob =
            viewModelScope.launch {
                val oldData =
                    when (_pokeNotification.value is UiState.Success) {
                        true -> (_pokeNotification.value as UiState.Success<List<PokeUser>>).data
                        false -> emptyList()
                    }
                getPokeNotificationListUseCase.invoke(page = currentPaginationIndex)
                    .onSuccess {
                        totalPageSize = it.totalPageSize
                        currentPaginationIndex = it.pageNum
                        _pokeNotification.emit(UiState.Success(oldData.plus(it.history)))
                    }
                    .onApiError { statusCode, responseMessage ->
                        _pokeNotification.emit(UiState.ApiError(statusCode, responseMessage))
                    }
                    .onFailure { throwable ->
                        _pokeNotification.emit(UiState.Failure(throwable))
                    }
            }
    }

    fun pokeUser(userId: Int, isAnonymous: Boolean, message: String, isFirstMeet: Boolean) {
        viewModelScope.launch {
            pokeUserUseCase.invoke(
                userId = userId,
                isAnonymous = isAnonymous,
                message = message
            ).onSuccess { response ->
                _pokeUserUiState.emit(UiState.Success(response, isFirstMeet))
            }.onApiError { statusCode, responseMessage ->
                _pokeUserUiState.emit(UiState.ApiError(statusCode, responseMessage))
            }.onFailure { throwable ->
                _pokeUserUiState.emit(UiState.Failure(throwable))
            }
        }
    }

    fun setAnonymousFriend(pokeUser: PokeUser?) {
        _anonymousFriend.value = pokeUser
    }
}