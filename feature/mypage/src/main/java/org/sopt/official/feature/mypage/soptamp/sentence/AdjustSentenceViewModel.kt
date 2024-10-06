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
package org.sopt.official.feature.mypage.soptamp.sentence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.sopt.official.domain.mypage.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AdjustSentenceViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _sentence: MutableStateFlow<String> = MutableStateFlow("")
    val sentence: StateFlow<String> get() = _sentence.asStateFlow()

    private var initSentence: String = ""

    val isConfirmed = sentence.map { sentence ->
        initSentence != sentence
    }

    private val _sideEffect = MutableSharedFlow<AdjustSentenceSideEffect>()
    val sideEffect: SharedFlow<AdjustSentenceSideEffect> = _sideEffect.asSharedFlow()

    init {
        initAdjustSentence()
    }

    private fun initAdjustSentence() {
        viewModelScope.launch {
            userRepository.getUserInfo()
                .onSuccess { response ->
                    _sentence.value = response.profileMessage
                    initSentence = response.profileMessage
                }.onFailure(Timber::e)
        }
    }

    fun adjustSentence() {
        viewModelScope.launch {
            userRepository.updateProfileMessage(_sentence.value)
                .onSuccess {
                    _sideEffect.emit(AdjustSentenceSideEffect.NavigateToMyPage)
                }.onFailure(Timber::e)
        }
    }

    fun onChange(new: String) {
        _sentence.value = new
    }
}
