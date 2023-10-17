/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.sopt.official.domain.entity.auth.Auth
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.usecase.LoginUseCase
import timber.log.Timber
import javax.inject.Inject

sealed interface AuthUiEvent {
    data class Success(val userStatus: UserStatus) : AuthUiEvent
    data class Failure(val message: String) : AuthUiEvent
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    suspend fun onLogin(auth: Auth) {
        loginUseCase(auth)
        _uiEvent.emit(AuthUiEvent.Success(auth.status))
    }

    suspend fun onFailure(throwable: Throwable) {
        Timber.e(throwable)
        _uiEvent.emit(AuthUiEvent.Failure(throwable.message ?: "로그인에 실패했습니다."))
    }
}
