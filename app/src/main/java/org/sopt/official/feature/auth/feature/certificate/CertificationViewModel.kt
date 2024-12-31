package org.sopt.official.feature.auth.feature.certificate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.official.domain.auth.model.UserInformation
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
    suspend fun createPhoneNumber() {
        repository.getCertificationNumber(
            UserInformation(
                name = null,
                phone = "01012345678",
                type = CertificationType.REGISTER.type
            )
        ).onSuccess { }.onFailure { }
    }
}