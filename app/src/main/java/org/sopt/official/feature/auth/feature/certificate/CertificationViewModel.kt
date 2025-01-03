package org.sopt.official.feature.auth.feature.certificate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.auth.model.InformationWithCode
import org.sopt.official.domain.auth.model.InitialInformation
import org.sopt.official.domain.auth.model.UserPhoneNumber
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.feature.auth.model.AuthStatus
import javax.inject.Inject

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

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
                    code = status.code,
                    type = status.type
                )
            ).onSuccess {
                _sideEffect.emit(CertificationSideEffect.NavigateToSocialAccount)
            }.onFailure {
                _sideEffect.emit(CertificationSideEffect.ShowToast("실패ㅠㅠ"))
            }
        }
    }

    fun findAccount() {
        viewModelScope.launch {
            repository.findAccount(
                UserPhoneNumber(
                    phone = "01012345678"
                )
            ).onSuccess {
                _sideEffect.emit(CertificationSideEffect.ShowToast("성공!!!"))
            }.onFailure {
                _sideEffect.emit(CertificationSideEffect.ShowToast("실패ㅠㅠ"))
            }
        }
    }
}