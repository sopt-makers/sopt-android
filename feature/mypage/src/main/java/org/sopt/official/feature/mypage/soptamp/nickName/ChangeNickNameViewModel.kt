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
package org.sopt.official.feature.mypage.soptamp.nickName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import org.sopt.official.common.util.rx.subscribeBy
import org.sopt.official.common.util.rx.subscribeOnIo
import org.sopt.official.domain.soptamp.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChangeNickNameViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val backPressedSignal = PublishSubject.create<Boolean>()
    val nickName = BehaviorProcessor.createDefault("")
    val isValidNickName = BehaviorProcessor.createDefault(true)

    private val createDisposable = CompositeDisposable()

    init {
        validateNickName()
    }

    fun changeNickName() {
        viewModelScope.launch {
            nickName.first("")
                .subscribeOnIo()
                .subscribeBy(
                    createDisposable,
                    onSuccess = {
                        backPressedSignal.onNext(true)
                    }
                )
        }
    }

    private fun validateNickName() {
        nickName.first("")
            .subscribeOnIo()
            .subscribeBy(
                createDisposable,
                onSuccess = {
                    if (it.isBlank()) {
                        isValidNickName.onNext(true)
                    } else {
                        checkNickName(it)
                    }
                }
            )
    }

    private fun checkNickName(nickName: String) {
        viewModelScope.launch {
            userRepository.checkNickname(nickName)
                .onSuccess {
                    isValidNickName.onNext(true)
                }.onFailure {
                    Timber.e(it)
                    isValidNickName.onNext(false)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()

        createDisposable.dispose()
    }
}
