package org.sopt.official.feature.fortune.feature.fortuneDetail.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface FortuneDetailUiState {

    @Immutable
    data class TodaySentence(
        val userName: String,
        val content: String,
    ) : FortuneDetailUiState {
        val message: String
            get() = "${userName}님,\n${content}"
    }

    @Immutable
    data object Loading : FortuneDetailUiState

    @Immutable
    data class Error(val errorMessage: Throwable) : FortuneDetailUiState
}
