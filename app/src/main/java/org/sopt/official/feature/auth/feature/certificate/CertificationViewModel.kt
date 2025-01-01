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
import org.sopt.official.domain.auth.repository.AuthRepository
import javax.inject.Inject

enum class CertificationType(val type: String) {
    REGISTER("REGISTER"),
    CHANGE("CHANGE"),
    SEARCH("SEARCH")
}

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<CertificationSideEffect>()
    val sideEffect: SharedFlow<CertificationSideEffect> = _sideEffect.asSharedFlow()
    fun createCode() {
        viewModelScope.launch {
            repository.createCode(
                InitialInformation(
                    name = "Mock-Success-Register",
                    phone = "01012345678",
                    type = CertificationType.REGISTER.type
                )
            ).onSuccess {
                _sideEffect.emit(CertificationSideEffect.ShowToast("성공!!!"))
            }.onFailure {
                _sideEffect.emit(CertificationSideEffect.ShowToast("실패ㅠㅠ"))
            }
        }
    }

    fun certificateCode() {
        viewModelScope.launch {
            repository.certificateCode(
                InformationWithCode(
                    name = "Mock-Success-Register",
                    phone = "01012345678",
                    code = "123456",
                    type = CertificationType.REGISTER.type
                )
            ).onSuccess {
                _sideEffect.emit(CertificationSideEffect.ShowToast("성공!!!"))
            }.onFailure {
                _sideEffect.emit(CertificationSideEffect.ShowToast("실패ㅠㅠ"))
            }
        }
    }
}