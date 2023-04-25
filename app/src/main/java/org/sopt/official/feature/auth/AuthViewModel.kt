package org.sopt.official.feature.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.sopt.official.domain.entity.auth.Auth
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.usecase.LoginUseCase
import timber.log.Timber
import javax.inject.Inject

sealed interface AuthUiEvent {
    data class Success(val userStatus: UserStatus) : AuthUiEvent
    data class Failure(val message: String) : AuthUiEvent
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    suspend fun onLogin(auth: Auth) {
        loginUseCase(auth)
        _uiEvent.emit(AuthUiEvent.Success(auth.status))
    }

    suspend fun onFailure(throwable: Throwable) {
        Timber.e(throwable)
        _uiEvent.emit(AuthUiEvent.Failure(throwable.message ?: "로그인에 실패했습니다."))
    }
}
