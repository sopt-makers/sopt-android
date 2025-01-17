package org.sopt.official.feature.auth.feature.certificate

sealed class CertificationSideEffect {
    data class ShowToast(val message: String) : CertificationSideEffect()
    data class NavigateToSocialAccount(val name: String) : CertificationSideEffect()
    data class NavigateToAuthMain(val platform: String) : CertificationSideEffect()
}