/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.auth.feature.certificate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
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
import org.sopt.official.domain.auth.model.User
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.feature.auth.feature.certificate.model.ErrorResponse
import org.sopt.official.feature.auth.model.AuthStatus
import org.sopt.official.network.persistence.SoptDataStore
import retrofit2.HttpException
import javax.inject.Inject

internal enum class ErrorCase(val message: String) {
    // 전화번호 인증 에러
    PHONE_ERROR("SOPT 활동 시 사용한 전화번호가 아니에요."),
    NOT_FOUND("가입 정보를 찾을 수 없습니다."),
    NUMBER_ALREADY_EXISTS("이미 가입된 전화번호입니다."),
    PHONE_UNKNOWN_ERROR("알 수 없는 오류예요."),

    // 코드 인증 에러
    CODE_ERROR("인증번호가 일치하지 않습니다."),
    TIME_ERROR("3분이 초과되었어요. 인증번호를 다시 요청해주세요."),
    CODE_UNKNOWN_ERROR("알 수 없는 오류예요."), ;

    companion object {
        fun fromMessage(message: String): ErrorCase? = entries.firstOrNull { it.message == message }
        fun isPhoneError(message: String) =
            persistentListOf(PHONE_ERROR, NOT_FOUND, NUMBER_ALREADY_EXISTS, PHONE_UNKNOWN_ERROR).any { it.message == message }

        fun isCodeError(message: String) = persistentListOf(CODE_ERROR, TIME_ERROR, CODE_UNKNOWN_ERROR).any { it.message == message }
    }
}

internal enum class CertificationButtonText(val message: String) {
    GET_CODE("전송하기"),
    CHANGE_CODE("재전송하기")
}

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStore: SoptDataStore,
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
                User(
                    phone = _state.value.phone,
                    type = status.type
                ),
            ).onSuccess {
                startTimer()
                updateButtonText()
                updateCodeTextField(true)
                updateButtonState(true)
            }.onFailure { throwable ->
                updateCodeTextField(false)
                updateButtonState(false)

                when (throwable) {
                    is HttpException -> {
                        val errorBody = throwable.response()?.errorBody()?.string()
                        val gson = Gson()
                        val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                        val errorMessage = errorResponse.message

                        _state.update { currentState ->
                            currentState.copy(
                                errorMessage = ErrorCase.fromMessage(errorMessage)?.message ?: ErrorCase.PHONE_UNKNOWN_ERROR.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun certificateCode(status: AuthStatus) {
        viewModelScope.launch {
            repository.certificateCode(
                User(
                    phone = state.value.phone,
                    code = state.value.code,
                    type = status.type
                )
            ).onSuccess { response ->
                if (status.type == AuthStatus.SEARCH_SOCIAL_PLATFORM.type) {
                    findAccount(name = response.name)
                } else {
                    _sideEffect.emit(
                        CertificationSideEffect.NavigateToSocialAccount(
                            name = response.name,
                            phone = response.phone
                        )
                    )
                }
            }.onFailure { throwable ->
                updateCodeTextField(false)

                when (throwable) {
                    is HttpException -> {
                        val errorBody = throwable.response()?.errorBody()?.string()
                        val gson = Gson()
                        val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                        val errorMessage = errorResponse.message

                        _state.update { currentState ->
                            currentState.copy(
                                errorMessage = ErrorCase.fromMessage(errorMessage)?.message ?: ErrorCase.CODE_UNKNOWN_ERROR.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun findAccount(name: String) {
        viewModelScope.launch {
            repository.findAccount(
                name = name,
                phone = state.value.phone,
            ).onSuccess { response ->
                _sideEffect.emit(CertificationSideEffect.NavigateToAuthMain(response.authPlatform))
                dataStore.platform = response.authPlatform
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

    private fun updateCodeTextField(isEnable: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isCodeEnable = isEnable
            )
        }
    }

    private fun updateButtonState(isEnable: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isButtonEnable = isEnable
            )
        }
    }

}
