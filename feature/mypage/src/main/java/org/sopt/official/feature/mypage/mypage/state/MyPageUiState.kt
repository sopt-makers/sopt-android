package org.sopt.official.feature.mypage.mypage.state

import org.sopt.official.auth.model.UserActiveState

data class MyPageUiState(
    val user: UserActiveState,
    val dialogState: MyPageDialogState,
    val onEventSink: (action: MyPageAction) -> Unit,
)

enum class MyPageDialogState {
    CLEAR_SOPTAMP, LOGOUT, CLEAR;
}
