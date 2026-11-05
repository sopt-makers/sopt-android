/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
