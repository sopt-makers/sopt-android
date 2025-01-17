package org.sopt.official.feature.auth.feature.authmain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.auth.model.SignInCode
import org.sopt.official.domain.auth.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthMainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<AuthMainSideEffect>()
    val sideEffect: SharedFlow<AuthMainSideEffect> = _sideEffect.asSharedFlow()

    fun signIn(token: String) {
        // TODO: 실제 token 넣기 by leeeyubin
        viewModelScope.launch {
            authRepository.signIn(
                SignInCode(
                    token = "codecodecodecodecode",
                    authPlatform = GOOGLE
                )
            ).onSuccess {
                //TODO: 홈 화면으로 이동
                _sideEffect.emit(AuthMainSideEffect.ShowToast("성공"))
            }.onFailure {
                _sideEffect.emit(AuthMainSideEffect.ShowToast("실패"))
            }
        }
    }

    companion object {
        private const val GOOGLE = "GOOGLE"
    }
}