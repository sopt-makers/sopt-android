package org.sopt.official.feature.mypage.model

import org.sopt.official.domain.entity.UserActiveState

sealed interface MyPageUiState {
    data object UnInitialized : MyPageUiState
    data class User(val activeState: UserActiveState) : MyPageUiState
}
