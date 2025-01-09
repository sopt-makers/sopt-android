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
package org.sopt.official.feature.mypage.mypage.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.google.firebase.messaging.FirebaseMessaging
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.auth.model.UserActiveState
import org.sopt.official.auth.repository.AuthRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import timber.log.Timber

@Composable
fun rememberMyPageUiState(
    userActiveState: UserActiveState,
    authRepository: AuthRepository,
    stampRepository: StampRepository,
    onRestartApp: () -> Unit
): MyPageUiState {
    var userActiveState by rememberRetained { mutableStateOf(userActiveState) }
    var dialogState by rememberRetained { mutableStateOf(MyPageDialogState.CLEAR) }

    val scope = rememberCoroutineScope()

    return MyPageUiState(
        user = userActiveState,
        dialogState = dialogState,
        onEventSink = { action ->
            when (action) {
                is ResetSoptamp -> {
                    scope.launch {
                        stampRepository.deleteAllStamps()
                            .onSuccess { dialogState = MyPageDialogState.CLEAR }
                            .onFailure { Timber.e(it) }
                    }
                }

                is ClearSoptamp -> {
                    dialogState = MyPageDialogState.CLEAR_SOPTAMP
                }

                is Logout -> {
                    scope.launch {
                        runCatching {
                            FirebaseMessaging.getInstance().token.await()
                        }.onSuccess {
                            authRepository.logout(it)
                                .onSuccess {
                                    authRepository.clearLocalData()
                                    onRestartApp()
                                }.onFailure { error ->
                                    Timber.e(error)
                                }
                        }.onFailure {
                            Timber.e(it)
                        }
                    }
                }

                is CloseDialog -> {
                    dialogState = MyPageDialogState.CLEAR
                }
            }
        }
    )
}
