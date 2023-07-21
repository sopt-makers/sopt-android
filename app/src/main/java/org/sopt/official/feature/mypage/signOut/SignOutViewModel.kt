package org.sopt.official.feature.mypage.signOut

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.processors.PublishProcessor
import kotlinx.coroutines.launch
import org.sopt.official.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val restartSignal = PublishProcessor.create<Boolean>()

    fun signOut() {
        viewModelScope.launch {
            authRepository.withdraw()
                .onSuccess {
                    restartSignal.onNext(true)
                }.onFailure {
                    Timber.e(it)
                }
        }
    }
}