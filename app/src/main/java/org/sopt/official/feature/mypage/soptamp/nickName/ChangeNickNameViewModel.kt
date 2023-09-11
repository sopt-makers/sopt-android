package org.sopt.official.feature.mypage.soptamp.nickName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import org.sopt.official.stamp.domain.repository.UserRepository
import org.sopt.official.util.rx.subscribeBy
import org.sopt.official.util.rx.subscribeOnIo
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