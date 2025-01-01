package org.sopt.official.feature.mypage.mypage.state

sealed interface MyPageAction
data object ResetSoptamp : MyPageAction
data object ClearSoptamp : MyPageAction
data object Logout : MyPageAction
data object CloseDialog : MyPageAction