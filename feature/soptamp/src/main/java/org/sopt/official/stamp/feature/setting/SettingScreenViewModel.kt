/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.sopt.official.stamp.domain.repository.StampRepository
import org.sopt.official.stamp.domain.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

enum class SettingScreenAction {
    CLEAR_ALL_STAMP, LOGOUT;
}

sealed interface SettingUiState {
    data class Success(val action: SettingScreenAction) : SettingUiState
    data class Failure(val throwable: Throwable) : SettingUiState
    data class Dialog(val action: SettingScreenAction) : SettingUiState
    object Init : SettingUiState
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
