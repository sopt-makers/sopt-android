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
package org.sopt.official.feature.poke.v2.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

/**
 * 아직 비즈니스 로직이 이관되지 않은 화면들의 라우트.
 *
 * 브릿지·메인에서 이 화면들로의 이동 경로만 먼저 확보해 둔다. 각 화면 이관이 끝나면
 * 해당 화면 패키지의 `navigation/` 하위로 옮긴다.
 *
 * 친한 친구 리스트는 [org.sopt.official.feature.poke.v2.friend.navigation.PokeFriend] 를 사용한다.
 */

/**
 * 콕 찌르기 알림 화면.
 *
 * @property userStatus [org.sopt.official.model.UserStatus] 이름.
 */
@Serializable
data class PokeNotification(
    val userStatus: String = "",
) : Route

fun NavController.navigateToPokeNotification(
    userStatus: String,
    navOptions: NavOptions? = null,
) {
    navigate(PokeNotification(userStatus), navOptions)
}
