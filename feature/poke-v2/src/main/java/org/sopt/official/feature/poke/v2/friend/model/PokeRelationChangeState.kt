package org.sopt.official.feature.poke.v2.friend.model

import androidx.compose.runtime.Immutable
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState

/**
 * 콕찌르기 이후 관계 변화 시 보여주는 로띠 진행 단계
 *
 * - 단짝친구: BestFriend
 * - 천생연분: Soulmate -> 로띠 끝나면 SoulmateRevealed(정체 공개)
 *
 * @property user   찌른 대상 친구
 */

sealed interface PokeRelationChangeState {
    val user: PokeUserUiState

    @Immutable
    data class BestFriend(
        override val user: PokeUserUiState
    ) : PokeRelationChangeState

    @Immutable
    data class Soulmate(
        override val user: PokeUserUiState
    ) : PokeRelationChangeState

    @Immutable
    data class SoulmateRevealed(
        override val user: PokeUserUiState
    ) : PokeRelationChangeState
}
