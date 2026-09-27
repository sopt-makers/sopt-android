package org.sopt.official.feature.poke.v2.main.model

import androidx.compose.runtime.Immutable
import org.sopt.official.domain.poke.entity.PokeUser

/**
 * 받은 콕 메시지 행의 UI 상태
 *
 * @property user             찌른 유저
 * @property message          받은 콕 메시지
 * @property relationTagText  관계 태그 문구 (비어 있으면 미표시)
 */
@Immutable
data class PokeMessageRowUiState(
    val user: PokeUserUiState,
    val message: String,
    val relationTagText: String,
)

fun PokeUser.toPokeMessageRowUiState(): PokeMessageRowUiState {
    val user = toPokeUserUiState()
    return PokeMessageRowUiState(
        user = user,
        message = message,
        relationTagText = when {
            user.isAnonymousVisible -> ""
            isFirstMeet -> mutualRelationMessage
            else -> "$relationName ${pokeNum}콕"
        },
    )
}
