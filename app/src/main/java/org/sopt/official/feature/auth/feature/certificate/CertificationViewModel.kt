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
import okhttp3.internal.immutableListOf
import org.sopt.official.domain.auth.model.InformationWithCode
import org.sopt.official.domain.auth.model.InitialInformation
import org.sopt.official.domain.auth.model.UserPhoneNumber
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.feature.auth.model.AuthStatus
import javax.inject.Inject

internal enum class ErrorCase(val message: String) {
    CODE_ERROR("인증 번호가 일치하지 않아요."),
    PHONE_ERROR("SOPT 활동 시 사용한 전화번호가 아니에요."),
    TIME_ERROR("3분이 초과되었어요. 인증번호를 다시 요청해주세요.");

    companion object {
        fun isPhoneError(message: String) = PHONE_ERROR.message == message
        fun isCodeError(message: String) = immutableListOf(CODE_ERROR, TIME_ERROR).any { it.message == message }
    }
}

internal enum class CertificationButtonText(val message: String) {
    GET_CODE("인증번호 요청"),
    CHANGE_CODE("재전송하기")
}

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state: MutableStateFlow<CertificationState> =
        MutableStateFlow(CertificationState())
    val state: StateFlow<CertificationState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CertificationSideEffect>()
    val sideEffect: SharedFlow<CertificationSideEffect> = _sideEffect.asSharedFlow()

    var timerJob: Job? = null

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
                startTimer()
                updateButtonText()
                updateCodeTextField(true)
                updateButtonState(true)
            }.onFailure {
                // TODO: DELETE !!
                startTimer()
                updateButtonText()

                updateCodeTextField(false)
                updateButtonState(false)
                // TODO: 주석 해제
//                _state.update { currentState ->
//                    currentState.copy(
//                        errorMessage = ErrorCase.PHONE_ERROR.message
//                    )
//                }
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
                _sideEffect.emit(CertificationSideEffect.ShowToast("실패"))
            }
        }
    }

    private fun findAccount() {
        viewModelScope.launch {
            repository.findAccount(
                UserPhoneNumber(
                    phone = "01012345678"
                )
            ).onSuccess { response ->
                _sideEffect.emit(CertificationSideEffect.NavigateToAuthMain(response.platform))
            }.onFailure {
                _sideEffect.emit(CertificationSideEffect.ShowToast("실패"))
            }
        }
    }

    private suspend fun startTimer() {
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

                if (_state.value.isTimerEnd) {
                    _state.update { currentState ->
                        currentState.copy(
                            errorMessage = ErrorCase.TIME_ERROR.message
                        )
                    }

                    timerJob?.cancelAndJoin()
                    timerJob = null
                } else {
                    _state.update { currentState ->
                        currentState.copy(
                            currentTimeValue = currentState.currentTimeValue - 1
                        )
                    }
                }
            }
        }
    }

    fun resetErrorCase() {
        _state.update { currentState ->
            currentState.copy(
                errorMessage = ""
            )
        }
    }

    private fun updateButtonText() {
        _state.update { currentState ->
            currentState.copy(
                buttonText = CertificationButtonText.CHANGE_CODE.message
            )
        }
    }

    private fun updateCodeTextField(isEnable: Boolean){
        _state.update { currentState ->
            currentState.copy(
                isCodeEnable = isEnable
            )
        }
    }

    private fun updateButtonState(isEnable : Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isButtonEnable = isEnable
            )
        }
    }

}
