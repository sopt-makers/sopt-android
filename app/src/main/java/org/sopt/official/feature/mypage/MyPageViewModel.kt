package org.sopt.official.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import org.sopt.official.domain.repository.AuthRepository
import org.sopt.official.feature.mypage.model.MyPageUiState
import org.sopt.official.stamp.domain.repository.StampRepository
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
            authRepository.logout()
                .onSuccess {
                    restartSignal.onNext(true)
                }.onFailure {
                    Timber.e(it)
                }
        }
    }

    fun resetSoptamp() {
        viewModelScope.launch {
            stampRepository.deleteAllStamps()
                .onSuccess {
                }.onFailure {
                    Timber.e(it)
                }
        }
    }
}