package org.sopt.official.feature.auth.feature.certificate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.sopt.official.domain.auth.model.InformationWithCode
import org.sopt.official.domain.auth.model.InitialInformation
import org.sopt.official.domain.auth.model.UserPhoneNumber
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.feature.auth.model.AuthStatus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CertificationState())
    val state: StateFlow<CertificationState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CertificationSideEffect>()
    val sideEffect: SharedFlow<CertificationSideEffect> = _sideEffect.asSharedFlow()

    private var timerJob: Job? = null

    fun updatePhone(phone: String) {
        _state.update { currentState ->
            currentState.copy(
                phone = phone
            )
        }
    }

    fun updateCode(code: String) {
        _state.update { currentState ->
            currentState.copy(
                code = code
            )
        }
    }

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

    suspend fun startTimer() {
        timerJob?.cancelAndJoin()
        timerJob = null
        _state.update { currentState ->
            currentState.copy(
                currentTimeValue = 180
            )
        }
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                Timber.d("totalSeconds: ${state.value}")

                _state.update { currentState ->
                    currentState.copy(
                        currentTimeValue = currentState.currentTimeValue - 1
                    )
                }
            }
            delay(1000L)
        }
    }
}
