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
package org.sopt.official.stamp.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.officiail.domain.mypage.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

enum class SettingScreenAction {
    CLEAR_ALL_STAMP, LOGOUT;
}

sealed interface SettingUiState {
    data class Success(val action: SettingScreenAction) : SettingUiState
    data class Failure(val throwable: Throwable) : SettingUiState
    data class Dialog(val action: SettingScreenAction) : SettingUiState
    data object Init : SettingUiState
}

@HiltViewModel
class SettingScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stampRepository: StampRepository
) : ViewModel() {
    private val _uiState = MutableSharedFlow<SettingUiState?>(extraBufferCapacity = 1)
    val uiState = _uiState.asSharedFlow()
    fun onClearAllStamps() {
        viewModelScope.launch {
            stampRepository.deleteAllStamps()
                .onSuccess {
                    _uiState.emit(SettingUiState.Success(SettingScreenAction.CLEAR_ALL_STAMP))
                }.onFailure {
                    Timber.e(it)
                    _uiState.emit(SettingUiState.Failure(it))
                }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            userRepository.logout()
                .onSuccess {
                    _uiState.emit(SettingUiState.Success(SettingScreenAction.LOGOUT))
                }.onFailure {
                    Timber.e(it)
                    _uiState.emit(SettingUiState.Failure(it))
                }
        }
    }

    fun onShowDialog(action: SettingScreenAction) {
        _uiState.tryEmit(SettingUiState.Dialog(action))
    }

    fun onDismiss() {
        _uiState.tryEmit(SettingUiState.Init)
    }
}
