/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.mypage.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.auth.model.UserActiveState
import org.sopt.official.auth.repository.AuthRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.feature.mypage.model.MyPageUiState
import timber.log.Timber
import javax.inject.Inject

enum class MyPageAction {
    CLEAR_SOPTAMP, LOGOUT
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val stampRepository: StampRepository,
) : ViewModel() {

    private val _userActiveState = MutableStateFlow<MyPageUiState>(MyPageUiState.UnInitialized)
    val userActiveState = _userActiveState.filterIsInstance<MyPageUiState.User>()
        .map { it.activeState != UserActiveState.UNAUTHENTICATED }

    private val _dialogState: MutableStateFlow<MyPageUiState> = MutableStateFlow(MyPageUiState.UnInitialized)
    val dialogState: StateFlow<MyPageUiState> = _dialogState.asStateFlow()

    private val _finish = Channel<Unit>()
    val finish = _finish.receiveAsFlow()

    fun setUserActiveState(new: MyPageUiState) {
        _userActiveState.value = new
    }

    fun logOut() {
        viewModelScope.launch {
            runCatching {
                FirebaseMessaging.getInstance().token.await()
            }.onSuccess {
                authRepository.logout(it)
                    .onSuccess {
                        authRepository.clearLocalData()
                        _finish.send(Unit)
                    }.onFailure { error -> Timber.e(error) }
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    fun resetSoptamp() {
        viewModelScope.launch {
            stampRepository.deleteAllStamps()
                .onFailure { Timber.e(it) }
        }
    }

    fun showDialogState(action: MyPageAction) {
        _dialogState.tryEmit(MyPageUiState.Dialog(action))
    }

    fun onDismiss() {
        _dialogState.tryEmit(MyPageUiState.UnInitialized)
    }

}
