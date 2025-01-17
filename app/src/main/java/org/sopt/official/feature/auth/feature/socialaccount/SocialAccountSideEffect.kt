package org.sopt.official.feature.auth.feature.socialaccount

sealed class SocialAccountSideEffect {
    data class ShowToast(val message: String) : SocialAccountSideEffect()
}