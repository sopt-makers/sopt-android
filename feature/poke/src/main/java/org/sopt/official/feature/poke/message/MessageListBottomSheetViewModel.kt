/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.poke.entity.PokeMessageList
import org.sopt.official.domain.poke.entity.onApiError
import org.sopt.official.domain.poke.entity.onFailure
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.type.PokeMessageType
import org.sopt.official.domain.poke.usecase.GetPokeMessageListUseCase
import org.sopt.official.feature.poke.UiState

@ViewModelKey(MessageListBottomSheetViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class MessageListBottomSheetViewModel(
    private val getPokeMessageListUseCase: GetPokeMessageListUseCase,
) : ViewModel() {
    private val _pokeMessageListUiState = MutableStateFlow<UiState<PokeMessageList>>(UiState.Loading)
    val pokeMessageListUiState: StateFlow<UiState<PokeMessageList>> get() = _pokeMessageListUiState

    private val _pokeAnonymousCheckboxChecked = MutableStateFlow(true)
    val pokeAnonymousCheckboxChecked: StateFlow<Boolean>
        get() = _pokeAnonymousCheckboxChecked

    val pokeAnonymousOffToast = MutableSharedFlow<Unit>()

    fun getPokeMessageList(pokeMessageType: PokeMessageType) {
        viewModelScope.launch {
            _pokeMessageListUiState.emit(UiState.Loading)
            getPokeMessageListUseCase.invoke(
                messageType = pokeMessageType,
            )
                .onSuccess { response ->
                    _pokeMessageListUiState.emit(UiState.Success(response))
                }
                .onApiError { statusCode, responseMessage ->
                    _pokeMessageListUiState.emit(UiState.ApiError(statusCode, responseMessage))
                }
                .onFailure { throwable ->
                    _pokeMessageListUiState.emit(UiState.Failure(throwable))
                }
        }
    }

    fun setPokeAnonymousCheckboxClicked() {
        _pokeAnonymousCheckboxChecked.value = !_pokeAnonymousCheckboxChecked.value

        if (!_pokeAnonymousCheckboxChecked.value) {
            viewModelScope.launch {
                pokeAnonymousOffToast.emit(Unit)
            }
        }
    }
}