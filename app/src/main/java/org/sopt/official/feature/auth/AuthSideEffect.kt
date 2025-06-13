package org.sopt.official.feature.auth

sealed class AuthSideEffect {
    data class ShowToast(val message: String) : AuthSideEffect()
}
