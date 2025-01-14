package org.sopt.official.feature.auth.feature.socialaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.auth.model.OriginalInformation
import org.sopt.official.domain.auth.model.SignUpCode
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.feature.auth.model.AuthStatus
import javax.inject.Inject

@HiltViewModel
class SocialAccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SocialAccountSideEffect>()
    val sideEffect: SharedFlow<SocialAccountSideEffect> = _sideEffect.asSharedFlow()
    fun connectSocialAccount(status: AuthStatus) {
        viewModelScope.launch {
            when (status) {
                // TODO: 실제 서버통신 값 넣기 by leeeyubin
                AuthStatus.REGISTER -> signUp()
                AuthStatus.CHANGE -> changeAccount()
                else -> {}
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            authRepository.signUp(
                SignUpCode(
                    name = "홍길동",
                    phone = "010-9121-2121",
                    code = "eyadxcvc.dasd.wda",
                    authPlatform = GOOGLE
                )
            ).onSuccess {
                _sideEffect.emit(SocialAccountSideEffect.ShowToast("성공!!"))
            }.onFailure {
                _sideEffect.emit(SocialAccountSideEffect.ShowToast("실패ㅠㅠ"))
            }
        }
    }

    private fun changeAccount() {
        viewModelScope.launch {
            authRepository.changeAccount(
                OriginalInformation(
                    phone = "010-0000-0000",
                    authPlatform = GOOGLE,
                    token = "codecodecodecodecode"
                )
            ).onSuccess {
                _sideEffect.emit(SocialAccountSideEffect.ShowToast("성공!!"))
            }.onFailure {
                _sideEffect.emit(SocialAccountSideEffect.ShowToast("실패ㅠㅠ"))
            }
        }
    }

    companion object {
        private const val GOOGLE = "GOOGLE"
    }
}