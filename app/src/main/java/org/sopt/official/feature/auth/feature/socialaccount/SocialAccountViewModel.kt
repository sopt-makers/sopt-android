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
package org.sopt.official.feature.auth.feature.socialaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.auth.model.Auth
import org.sopt.official.domain.auth.model.SignUpCode
import org.sopt.official.domain.auth.model.User
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.feature.auth.model.AuthStatus
import javax.inject.Inject

@HiltViewModel
class SocialAccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state: MutableStateFlow<SocialAccountState> = MutableStateFlow(SocialAccountState())
    val state: StateFlow<SocialAccountState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SocialAccountSideEffect>()
    val sideEffect: SharedFlow<SocialAccountSideEffect> = _sideEffect.asSharedFlow()

    fun updateInitialState(name: String, phone: String) {
        _state.update { currentState ->
            currentState.copy(
                name = name,
                phone = phone
            )
        }
    }

    fun connectSocialAccount(status: AuthStatus) {
        viewModelScope.launch {
            when (status) {
                AuthStatus.REGISTER -> signUp()
                AuthStatus.CHANGE_SOCIAL_PLATFORM -> changeAccount()
                else -> {}
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            authRepository.signUp(
                SignUpCode( // TODO: 실제 서버통신 값 넣기 by 이유빈
                    name = "홍길동",
                    phone = "010-9121-2121",
                    code = "eyadxcvc.dasd.wda",
                    authPlatform = GOOGLE
                )
            ).onSuccess {
                _sideEffect.emit(SocialAccountSideEffect.ShowToast("성공"))
            }.onFailure {
                _sideEffect.emit(SocialAccountSideEffect.ShowToast("실패"))
            }
        }
    }

    private fun changeAccount() {
        viewModelScope.launch {
            authRepository.changeAccount(
                userRequest = User(
                    phone = _state.value.phone,
                ),
                authRequest = Auth(
                    authPlatform = GOOGLE,
                    token = "codecodecodecodecode"
                )
            ).onSuccess {
                _sideEffect.emit(SocialAccountSideEffect.ShowToast("성공"))
            }.onFailure {
                _sideEffect.emit(SocialAccountSideEffect.ShowToast("실패"))
            }
        }
    }

    companion object {
        private const val GOOGLE = "GOOGLE"
    }
}
