package org.sopt.official.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.UserState
import org.sopt.official.domain.repository.AuthRepository
import org.sopt.official.stamp.domain.repository.StampRepository
import org.sopt.official.util.wrapper.NullableWrapper
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val stampRepository: StampRepository,
) : ViewModel() {
    val userState = MutableStateFlow(NullableWrapper.none<UserState>())

    val restartSignal = MutableSharedFlow<Unit>()

    fun logOut() {
        viewModelScope.launch {
            authRepository.logout()
                .onSuccess {
                    restartSignal.emit(Unit)
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