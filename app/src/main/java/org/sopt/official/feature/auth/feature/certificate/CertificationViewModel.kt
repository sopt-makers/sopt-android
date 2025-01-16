package org.sopt.official.feature.auth.feature.certificate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.auth.model.InformationWithCode
import org.sopt.official.domain.auth.model.InitialInformation
import org.sopt.official.domain.auth.model.UserPhoneNumber
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.feature.auth.model.AuthStatus
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CertificationState())
    val state: StateFlow<CertificationState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CertificationSideEffect>()
    val sideEffect: SharedFlow<CertificationSideEffect> = _sideEffect.asSharedFlow()
    fun createCode(status: AuthStatus) {
        viewModelScope.launch {
            repository.createCode(
                InitialInformation(
                    name = status.authName,
                    phone = status.phone,
                    type = status.type
                )
            ).onSuccess {
                _sideEffect.emit(CertificationSideEffect.ShowToast("성공!!!"))
            }.onFailure {
                _sideEffect.emit(CertificationSideEffect.ShowToast("실패ㅠㅠ"))
            }
        }
    }

    fun certificateCode(status: AuthStatus) {
        viewModelScope.launch {
            repository.certificateCode(
                InformationWithCode(
                    name = status.authName,
                    phone = status.phone,
                    code = status.token,
                    type = status.type
                )
            ).onSuccess { response ->
                if (status.type == AuthStatus.SEARCH.type) {
                    findAccount()
                } else {
                    _sideEffect.emit(CertificationSideEffect.NavigateToSocialAccount(response.name))
                }
            }.onFailure {
                _sideEffect.emit(CertificationSideEffect.ShowToast("실패ㅠㅠ"))
            }
        }
    }

    private fun findAccount() {
        viewModelScope.launch {
            repository.findAccount(
                UserPhoneNumber(
                    phone = "01012345678"
                )
            ).onSuccess {
                _sideEffect.emit(CertificationSideEffect.NavigateToAuthMain)
            }.onFailure {
                _sideEffect.emit(CertificationSideEffect.ShowToast("실패ㅠㅠ"))
            }
        }
    }

    fun startTimer(currentTime: String) {
        viewModelScope.launch {
            var (minutes, seconds) = currentTime.split(":").map { it.toInt() }
            var totalSeconds: Int = minutes * 60 + seconds

            while (totalSeconds > 0) {
                delay(1000L)

                totalSeconds--
                minutes = totalSeconds / 60
                seconds = totalSeconds % 60

                _state.update { currentState ->
                    currentState.copy(
                        currentTime = String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    )
                }
            }
            delay(1000L)
            // todo: 에러 화면 띄우기
        }
    }
}
