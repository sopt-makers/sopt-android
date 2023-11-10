/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import domain.repository.AuthRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.feature.mypage.model.MyPageUiState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val stampRepository: StampRepository,
) : ViewModel() {

    val userActiveState = BehaviorProcessor.createDefault<MyPageUiState>(MyPageUiState.UnInitialized)
    val restartSignal = PublishSubject.create<Boolean>()

    fun logOut() {
        viewModelScope.launch {
            runCatching {
                FirebaseMessaging.getInstance().token.await()
            }.onSuccess {
                authRepository.logout(it)
                    .onSuccess {
                        authRepository.clearLocalData()
                        restartSignal.onNext(true)
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
}
