package org.sopt.official.feature.auth.feature.authmain

sealed class AuthMainSideEffect {
    data class ShowToast(val message: String) : AuthMainSideEffect()
}