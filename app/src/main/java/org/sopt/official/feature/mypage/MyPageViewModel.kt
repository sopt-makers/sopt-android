/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.domain.repository.AuthRepository
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
