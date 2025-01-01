/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.mypage.soptamp.state

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.launch
import org.sopt.official.domain.mypage.repository.UserRepository
import timber.log.Timber

@Composable
fun rememberModifyProfileState(
    userRepository: UserRepository,
    onShowToast: (String) -> Unit
): ModifySoptampProfileUiState {
    var current by rememberRetained { mutableStateOf("") }
    var previous by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current
    val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

    LaunchedEffect(Unit) {
        userRepository.getUserInfo()
            .onSuccess {
                current = it.profileMessage
                previous = it.profileMessage
            }.onFailure {
                Timber.e(it)
            }
    }

    return ModifySoptampProfileUiState(
        current = current,
        previous = previous,
        onChangeCurrent = { current = it },
        onUpdate = {
            scope.launch {
                userRepository.updateProfileMessage(current)
                    .onSuccess {
                        keyboardController?.hide()
                        onShowToast("한마디가 변경되었습니다")
                        onBackPressedDispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
                    }.onFailure {
                        Timber.e(it)
                    }
            }
        }
    )
}
