package org.sopt.official.feature.mypage.soptamp.sentence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.stamp.domain.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AdjustSentenceViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val backPressedSignal = MutableStateFlow(false)

    fun adjustSentence(message: String) {
        viewModelScope.launch {
            userRepository.updateProfileMessage(message)
                .onSuccess {
                    backPressedSignal.value = true
                }.onFailure {
                    Timber.e(it)
                }
        }
    }
}