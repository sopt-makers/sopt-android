package org.sopt.official.feature.fortune.feature.fortuneDetail.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface FortuneDetailUiState {

    @Immutable
    data class Success(
        val todaySentence: TodaySentence,
        val userInfo: UserInfo,
    ) : FortuneDetailUiState {

        @Immutable
        data class TodaySentence(
            val userName: String,
            val content: String,
        ) {
            val message: String get() = "${userName}님,\n${content}"
        }

        @Immutable
        data class UserInfo(
            val userId: Long,
            val profile: String,
            val userName: String,
            val generation: Int,
            val part: String,
        ) {
            val userDescription = "${generation}기 $part"
        }
    }

    @Immutable
    data object Loading : FortuneDetailUiState

    @Immutable
    data class Error(val errorMessage: Throwable) : FortuneDetailUiState
}
