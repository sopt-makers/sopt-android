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
