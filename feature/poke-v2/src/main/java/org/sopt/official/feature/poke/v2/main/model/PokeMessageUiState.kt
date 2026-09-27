package org.sopt.official.feature.poke.v2.main.model

import androidx.compose.runtime.Immutable
import org.sopt.official.domain.poke.entity.PokeMessageList

/**
 * 콕찌르기 메시지 한 건의 UI 상태
 *
 * @property messageId  메시지 ID
 * @property content    메시지 내용
 */
@Immutable
data class PokeMessageUiState(
    val messageId: Int,
    val content: String
)

fun PokeMessageList.PokeMessage.toPokeMessageUiState() = PokeMessageUiState(
    messageId = messageId,
    content = content
)
