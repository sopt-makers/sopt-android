/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.domain.notification.repository.NotificationRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.domain.soptlog.repository.SoptLogRepository
import org.sopt.official.domain.user.repository.SoptUserRepository
import org.sopt.official.feature.mypage.model.MyPageDialogState
import org.sopt.official.localstorage.source.UserStorage
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userStorage: UserStorage,
    private val authRepository: AuthRepository,
    private val stampRepository: StampRepository,
    private val notificationRepository: NotificationRepository,
    private val soptUserRepository: SoptUserRepository,
    private val soptLogRepository: SoptLogRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MyPageState())
    val state: StateFlow<MyPageState> = _state.asStateFlow()

    val isAppjamMode: StateFlow<Boolean> = userStorage.isAppjamMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _sideEffect = Channel<MyPageSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            userStorage.userStatus.collect { status ->
                _state.update { it.copy(userStatus = status) }
            }
        }
        viewModelScope.launch {
            soptUserRepository.userInfo.collect { userInfo ->
                if (userInfo != null) {
                    _state.update {
                        it.copy(
                            name = userInfo.user.name,
                            profileImage = userInfo.user.profileImage,
                            part = userInfo.user.part,
                        )
                    }
                } else {
                    soptUserRepository.getUserInfo()
                        .onSuccess { result ->
                            _state.update {
                                it.copy(
                                    name = result.user.name,
                                    profileImage = result.user.profileImage,
                                    part = result.user.part,
                                )
                            }
                        }
                        .onFailure { Timber.e(it) }
                }
            }
        }
        viewModelScope.launch {
            soptLogRepository.soptLogInfo.collect { info ->
                if (info != null) {
                    _state.update {
                        it.copy(
                            soptampCount = info.soptampCount,
                            totalPokeCount = info.pokeCount,
                        )
                    }
                } else {
                    soptLogRepository.getSoptLogInfo()
                        .onSuccess { result ->
                            _state.update {
                                it.copy(
                                    soptampCount = result.soptampCount,
                                    totalPokeCount = result.pokeCount,
                                )
                            }
                        }
                        .onFailure { Timber.e(it) }
                }
            }
        }
    }

    fun onAction(action: MyPageAction) {
        when (action) {
            is MyPageAction.ClearSoptamp -> _state.update { it.copy(dialogState = MyPageDialogState.CLEAR_SOPTAMP) }
            is MyPageAction.ResetSoptamp -> resetSoptamp()
            is MyPageAction.RequestLogout -> _state.update { it.copy(dialogState = MyPageDialogState.REQUEST_LOGOUT) }
            is MyPageAction.ConfirmLogout -> confirmLogout()
            is MyPageAction.CloseDialog -> _state.update { it.copy(dialogState = MyPageDialogState.CLEAR) }
        }
    }

    private fun resetSoptamp() {
        viewModelScope.launch {
            stampRepository.deleteAllStamps()
                .onSuccess { _state.update { it.copy(dialogState = MyPageDialogState.CLEAR) } }
                .onFailure { Timber.e(it) }
        }
    }

    private fun confirmLogout() {
        viewModelScope.launch {
            runCatching {
                val pushToken = FirebaseMessaging.getInstance().token.await()
                notificationRepository.deleteToken(pushToken)
            }.onFailure { Timber.e(it) }

            withContext(Dispatchers.IO) {
                authRepository.clearUserToken()
            }

            soptLogRepository.invalidate()
            soptUserRepository.invalidate()

            _sideEffect.send(MyPageSideEffect.NavigateToAuth)
        }
    }
}
