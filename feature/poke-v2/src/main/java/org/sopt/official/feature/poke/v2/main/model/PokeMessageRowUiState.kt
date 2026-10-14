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
