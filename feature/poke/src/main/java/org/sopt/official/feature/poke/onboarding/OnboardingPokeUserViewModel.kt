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
package org.sopt.official.feature.poke.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
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
import org.sopt.official.domain.poke.usecase.PokeUserUseCase
import org.sopt.official.feature.poke.UiState

@ViewModelKey(OnboardingPokeUserViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class OnboardingPokeUserViewModel(
    private val getOnboardingPokeUserListUseCase: GetOnboardingPokeUserListUseCase,
    private val pokeUserUseCase: PokeUserUseCase,
) : ViewModel() {
    private val _onboardingPokeUserListUiState = MutableStateFlow<UiState<PokeRandomUserList.PokeRandomUsers>>(UiState.Loading)
    val onboardingPokeUserListUiState: StateFlow<UiState<PokeRandomUserList.PokeRandomUsers>> get() = _onboardingPokeUserListUiState

    private val _pokeUserUiState = MutableStateFlow<UiState<PokeUser>>(UiState.Loading)
    val pokeUserUiState: StateFlow<UiState<PokeUser>> get() = _pokeUserUiState
    private val randomType = MutableStateFlow("ALL")

    fun getOnboardingPokeUserList() {
        viewModelScope.launch {
            _onboardingPokeUserListUiState.emit(UiState.Loading)
            getOnboardingPokeUserListUseCase.invoke(randomType = randomType.value, size = 6)
                .onSuccess { response ->
                    _onboardingPokeUserListUiState.emit(UiState.Success(response.randomInfoList[0]))
                }
                .onApiError { statusCode, responseMessage ->
                    _onboardingPokeUserListUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _onboardingPokeUserListUiState.emit(UiState.Failure(throwable))
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
            if (_onboardingPokeUserListUiState.value is UiState.Success<PokeRandomUserList.PokeRandomUsers>) {
                val randomUsers = (_onboardingPokeUserListUiState.value as UiState.Success<PokeRandomUserList.PokeRandomUsers>).data
                val newList =
                    randomUsers.userInfoList
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
                _onboardingPokeUserListUiState.emit(UiState.Loading)
                launch {
                    _onboardingPokeUserListUiState.emit(
                        UiState.Success(
                            PokeRandomUserList.PokeRandomUsers(
                                randomType = randomUsers.randomType,
                                randomTitle = randomUsers.randomTitle,
                                userInfoList = newList,
                            )
                        )
                    )
                }
            }
        }
    }

    fun setRandomType(type: String) {
        randomType.value = type
    }
}