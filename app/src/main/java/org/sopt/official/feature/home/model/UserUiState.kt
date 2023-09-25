package org.sopt.official.feature.home.model

import androidx.annotation.StringRes

sealed interface UserUiState {
    data class User(
        @StringRes val title: Int,
        val userName: String,
        val period: String
    ) : UserUiState

    data class InactiveUser(@StringRes val title: Int) : UserUiState
    data class UnauthenticatedUser(@StringRes val title: Int) : UserUiState
}
