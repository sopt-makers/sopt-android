package org.sopt.official.feature.poke.v2.notification

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.poke.v2.main.model.PokeMessageUiState
import org.sopt.official.feature.poke.v2.main.model.PokeUserUiState

@Immutable
data class PokeNotificationUiState(
    // 메시지 선택 바텀시트
    val bottomSheetVisibility: Boolean = false,
    val target: PokeUserUiState? = null,
    val header: String = "함께 보낼 메시지를 선택해주세요",
    val pokeMessages: ImmutableList<PokeMessageUiState> = persistentListOf(),
    val isAnonymous: Boolean = true
)
