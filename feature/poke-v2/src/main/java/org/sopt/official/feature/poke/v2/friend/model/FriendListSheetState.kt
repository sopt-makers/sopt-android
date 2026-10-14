package org.sopt.official.feature.poke.v2.friend.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState

/**
 * 친구 목록 바텀시트의 UI 상태
 *
 * @property type           친구 유형
 * @property friendCount    해당 유형의 전체 친구 수
 * @property friends        친구 목록
 */

@Immutable
data class FriendListSheetState(
    val type: PokeFriendType,
    val friendCount: Int = 0,
    val friends: ImmutableList<PokeUserUiState> = persistentListOf(),
)
