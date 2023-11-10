/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.setting.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.officiail.domain.mypage.user.UpdateProfileUseCase
import javax.inject.Inject

data class EditIntroductionUiState(
    val introduction: String = "",
    val isFocused: Boolean = false,
    val isSuccess: Boolean = false,
    val error: Throwable? = null
)

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {
    private val uiState = MutableStateFlow(EditIntroductionUiState())
    val isFocused = uiState.map { it.isFocused }
    val introduction = uiState.map { it.introduction }
    val isSuccess = uiState.map { it.isSuccess }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)
    val error = uiState.map { it.error }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    fun onUpdateFocusState(isFocused: Boolean) {
        uiState.update { it.copy(isFocused = isFocused) }
    }

    fun onIntroductionChanged(introduction: String) {
        uiState.value = uiState.value.copy(introduction = introduction)
    }

    fun onSubmit() {
        viewModelScope.launch {
            uiState.update { it.copy(isSuccess = false) }
            updateProfileUseCase(uiState.value.introduction)
                .onSuccess {
                    uiState.update { it.copy(isSuccess = true) }
                }.onFailure { error ->
                    uiState.update { it.copy(isSuccess = false, error = error) }
                }
        }
    }
}
