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
package org.sopt.official.feature.mypage.soptamp.nickName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.mypage.repository.UserRepository
import timber.log.Timber

@HiltViewModel
class ChangeNickNameViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _finish = Channel<Unit>()
    val finish = _finish.receiveAsFlow()
    private val nickName = MutableStateFlow("")
    val isConfirmEnabled = nickName.map { it.isNotEmpty() }
    private val _isValidNickName = MutableStateFlow(true)
    val isValidNickName = _isValidNickName.asStateFlow()

    init {
        validateNickName()
    }

    fun onChangeNickName(new: String) {
        nickName.value = new
    }

    fun changeNickName() {
        viewModelScope.launch {
            userRepository.updateNickname(nickName.value)
                .onSuccess {
                    _finish.send(Unit)
                }.onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun validateNickName() {
        viewModelScope.launch {
            nickName.collect {
                if (it.isBlank()) {
                    _isValidNickName.value = true
                } else {
                    checkNickName(it)
                }
            }
        }
    }

    private fun checkNickName(nickName: String) {
        viewModelScope.launch {
            userRepository.checkNickname(nickName)
                .onSuccess {
                    _isValidNickName.value = true
                }.onFailure {
                    Timber.e(it)
                    _isValidNickName.value = false
                }
        }
    }
}
