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
package org.sopt.official.stamp.feature.setting.withdrawal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.sopt.official.stamp.domain.usecase.auth.WithdrawalUseCase
import javax.inject.Inject

@HiltViewModel
class WithdrawalScreenViewModel @Inject constructor(
    private val withdrawalUseCase: WithdrawalUseCase
) : ViewModel() {
    private val _isSuccess = MutableSharedFlow<Boolean>(1)
    val isSuccess = _isSuccess.asSharedFlow()
    private val _error = MutableSharedFlow<Throwable>(1)
    val error = _error.asSharedFlow()

    fun onPress() {
        viewModelScope.launch {
            withdrawalUseCase()
                .onSuccess { _isSuccess.tryEmit(true) }
                .onFailure { _error.emit(it) }
        }
    }
}
