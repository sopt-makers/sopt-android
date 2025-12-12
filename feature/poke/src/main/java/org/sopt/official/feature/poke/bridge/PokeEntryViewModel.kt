/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
