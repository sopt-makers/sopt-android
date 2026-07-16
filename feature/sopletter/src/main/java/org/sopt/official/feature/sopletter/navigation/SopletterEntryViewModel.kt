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
package org.sopt.official.feature.sopletter.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.sopt.official.domain.sopletter.onboarding.repository.SopletterOnboardingRepository
import org.sopt.official.localstorage.source.UserStorage
import javax.inject.Inject

@HiltViewModel
class SopletterEntryViewModel @Inject constructor(
    private val userStorage: UserStorage,
    private val sopletterOnboardingRepository: SopletterOnboardingRepository,
) : ViewModel() {
    private val _isOnboardingCompleted = MutableStateFlow<Boolean?>(null)
    val isOnboardingCompleted: StateFlow<Boolean?> = _isOnboardingCompleted.asStateFlow()

    init {
        observeOnboardingState()
        initializeOnboardingState()
    }

    private fun observeOnboardingState() = viewModelScope.launch {
        userStorage.isSopletterOnboardingCompleted.collectLatest { isCompleted ->
            if (isCompleted != null) {
                _isOnboardingCompleted.value = isCompleted
            }
        }
    }

    private fun initializeOnboardingState() = viewModelScope.launch {
        val localState = userStorage.isSopletterOnboardingCompleted.first()
        if (localState != null) {
            _isOnboardingCompleted.value = localState
            return@launch
        }

        sopletterOnboardingRepository.getOnboarding()
            .onSuccess { onboarding ->
                userStorage.saveOnboardingCompleted(onboarding.isOnboarded)
            }
            .onFailure {
                _isOnboardingCompleted.value = false
            }
    }
}
