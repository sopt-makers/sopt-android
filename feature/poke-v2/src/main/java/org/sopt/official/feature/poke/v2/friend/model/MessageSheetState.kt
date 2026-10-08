package org.sopt.official.feature.poke.v2.friend.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState

/**
 * 콕찌르기 메시지 선택 바텀시트의 UI 상태
 *
 * @property target         찌를 대상 친구
 * @property title          바텀시트 제목
 * @property messages       메시지 목록
 * @property isAnonymous    현재 익명 여부
 */

@Immutable
data class MessageSheetState(
    val target: PokeUserUiState,
    val title: String = "보낼 메시지를 골라주세요",
    val messages: ImmutableList<PokeMessageUiState> = persistentListOf(),
    val isAnonymous: Boolean = !target.isAnonymousCheckboxLocked,
)
