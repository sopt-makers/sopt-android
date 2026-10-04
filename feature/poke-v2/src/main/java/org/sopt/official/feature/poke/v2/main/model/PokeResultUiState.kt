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
 * 콕 찌르기 요청 성공 결과.
 *
 * 화면(Screen)은 이 값만 보고 어떤 연출(친구 완성 로띠 / 단짝·천생연분 로띠 / 단순 토스트)을
 * 보여줄지 결정한다. 레거시에서 `PokeScreen` 안에 흩어져 있던 판별 로직을 여기로 모았다.
 *
 * @property user            찌른 대상의 최신 UI 상태.
 * @property becameFriend     이번 콕으로 익명 → 친구가 된 순간인지. (`요청 시 isFirstMeet == true && 응답 isFirstMeet == false`)
 * @property becameBestFriend 이번 콕으로 단짝친구가 된 순간인지. (익명 & 5~6콕)
 * @property becameSoulmate   이번 콕으로 천생연분이 된 순간인지. (11~12콕)
 * @property anonymousName    익명 상대일 때 표시할 이름. 연출 문구에 사용.
 */
@Immutable
data class PokeResultUiState(
    val user: PokeUserUiState,
    val becameFriend: Boolean,
    val becameBestFriend: Boolean,
    val becameSoulmate: Boolean,
    val anonymousName: String,
) {
    /** 특별한 관계 변화 없이 그냥 콕만 보낸 경우(= 토스트만). */
    val isPlainPoke: Boolean
        get() = !becameFriend && !becameBestFriend && !becameSoulmate
}

/**
 * @param requestedFirstMeet 콕 요청 시 넘겼던 `isFirstMeet` 값(= 첫 만남 대상에게 보낸 콕인지).
 */
fun PokeUser.toPokeResultUiState(requestedFirstMeet: Boolean): PokeResultUiState {
    val userUiState = toPokeUserUiState()
    return PokeResultUiState(
        user = userUiState,
        becameFriend = requestedFirstMeet && !isFirstMeet,
        becameBestFriend = !(requestedFirstMeet && !isFirstMeet) && userUiState.isBestFriend,
        becameSoulmate = !(requestedFirstMeet && !isFirstMeet) && userUiState.isSoulMate,
        anonymousName = anonymousName,
    )
}
