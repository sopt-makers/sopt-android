package org.sopt.official.feature.mypage.model

import androidx.compose.runtime.Immutable

sealed interface MyPageUiModel {
    @Immutable
    data class Header(
        val title: String
    ) : MyPageUiModel

    @Immutable
    data class MyPageItem(
        val title: String,
        val onItemClick: () -> Unit
    ) : MyPageUiModel
}