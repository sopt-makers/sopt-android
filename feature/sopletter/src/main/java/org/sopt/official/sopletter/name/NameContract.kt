package org.sopt.official.sopletter.name

import androidx.compose.runtime.Immutable

// Todo : 추후 서버 반영
@Immutable
data class NameState(
    val name : String = "",
    val generation : Int = 0
)

sealed interface NameSideEffect {
    data object NavigateToSopletterMain : NameSideEffect
}