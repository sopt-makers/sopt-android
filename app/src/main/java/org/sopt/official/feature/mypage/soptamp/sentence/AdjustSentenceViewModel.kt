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
package org.sopt.official.feature.mypage.soptamp.sentence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import org.sopt.official.domain.soptamp.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AdjustSentenceViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val backPressedSignal = PublishSubject.create<Boolean>()

    fun adjustSentence(message: String) {
        viewModelScope.launch {
            userRepository.updateProfileMessage(message)
                .onSuccess {
                    backPressedSignal.onNext(true)
                }.onFailure {
                    Timber.e(it)
                }
        }
    }
}
