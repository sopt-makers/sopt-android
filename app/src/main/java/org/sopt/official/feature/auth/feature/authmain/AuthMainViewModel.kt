/*
 * MIT License
 * Copyright 2022-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.auth.feature.authmain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.auth.model.Auth
import org.sopt.official.domain.auth.repository.AuthRepository

@ViewModelKey(AuthMainViewModel::class)
@ContributesIntoMap(AppScope::class)
@Inject
class AuthMainViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<AuthMainSideEffect>()
    val sideEffect: SharedFlow<AuthMainSideEffect> = _sideEffect.asSharedFlow()

    fun signIn(token: String) {
        viewModelScope.launch {
            authRepository.signIn(
                Auth(
                    token = token,
                    authPlatform = GOOGLE,
                )
            ).onSuccess { response ->
                authRepository.saveUserToken(
                    accessToken = response.token,
                    refreshToken = response.refreshToken,
                )
                _sideEffect.emit(AuthMainSideEffect.NavigateToHome)
            }.onFailure {
                _sideEffect.emit(AuthMainSideEffect.NavigateToAuthError)
            }
        }
    }

    companion object {
        private const val GOOGLE = "GOOGLE"
    }
}