package org.sopt.official.feature.auth.feature.socialaccount

import org.sopt.official.feature.auth.model.AuthStatus

data class SocialAccountState(
    val status: String = "",
    val name: String = "",
    val phone: String = ""
) {
    val title: String
        get() = when (status) {
            AuthStatus.REGISTER.type -> "소셜 계정 연동"
            AuthStatus.CHANGE_SOCIAL_PLATFORM.type -> "소셜 계정 재설정"
            else -> ""
        }
}