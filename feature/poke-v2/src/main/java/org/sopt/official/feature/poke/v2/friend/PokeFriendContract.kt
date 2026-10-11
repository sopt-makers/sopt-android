package org.sopt.official.feature.poke.v2.friend

import androidx.compose.runtime.Immutable
import org.sopt.official.feature.poke.v2.component.PokeSnackBarType
import org.sopt.official.feature.poke.v2.friend.model.FriendListSheetState
import org.sopt.official.feature.poke.v2.friend.model.MessageSheetState
import org.sopt.official.feature.poke.v2.friend.model.PokeFriendListSections
import org.sopt.official.feature.poke.v2.friend.model.PokeRelationChangeState
import org.sopt.official.feature.poke.v2.friend.model.emptyPokeFriendListSections

@Immutable
data class PokeFriendState(
    val sections: PokeFriendListSections = emptyPokeFriendListSections(),
    val friendListSheet: FriendListSheetState? = null,
    val messageSheet: MessageSheetState? = null,
    val relationChange: PokeRelationChangeState? = null,
)

sealed interface PokeFriendSideEffect {
    data class ShowSnackbar(
        val message: String,
        val type: PokeSnackBarType = PokeSnackBarType.WARNING,
        val isMessageSheet: Boolean = false,
    ) : PokeFriendSideEffect
}
