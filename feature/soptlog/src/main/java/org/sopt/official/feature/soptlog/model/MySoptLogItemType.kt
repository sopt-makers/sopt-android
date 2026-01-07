/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.soptlog.model

import org.sopt.official.domain.soptlog.model.SoptLogInfo

internal enum class MySoptLogItemType(
    val title: String,
    val category: SoptLogCategory,
    val count: (SoptLogInfo) -> Int,
    val url: String = "",
    val hasHelpIcon: Boolean = false,
    val hasArrow: Boolean = true
) {
    // 솝탬프 로그
    // 일반 솝탬프의 경우는 (기존) url = "soptamp" / 앱잼탬프만 appjamtamp 사용 (앱잼탬프 기간만)
    COMPLETED_MISSION(title = "완료미션", category = SoptLogCategory.SOPTAMP, url = "appjamtamp", count = { it.soptampCount ?: 0 }),
    VIEW_COUNT(title = "조회수", category = SoptLogCategory.SOPTAMP, hasHelpIcon = true, hasArrow = false, count = { it.viewCount ?: 0 }),
    RECEIVED_CLAP(title = "받은 박수", category = SoptLogCategory.SOPTAMP, hasArrow = false, count = { it.myClapCount ?: 0 }),
    SENT_CLAP(title = "쳐준 박수", category = SoptLogCategory.SOPTAMP, hasArrow = false, count = { it.clapCount ?: 0 }),

    // 콕찌르기 로그
    TOTAL_POKE(title = "총 콕찌르기", category = SoptLogCategory.POKE, url = "home/poke", count = { it.pokeCount ?: 0 }),
    CLOSE_FRIEND(title = "친한 친구", category = SoptLogCategory.POKE, url = "home/poke/friend-list-summary?type=new", count = { it.newFriendsPokeCount ?: 0 }),
    BEST_FRIEND(title = "단짝 친구", category = SoptLogCategory.POKE, url = "home/poke/friend-list-summary?type=bestfriend", count = { it.bestFriendsPokeCount ?: 0 }),
    SOULMATE(title = "천생 연분", category = SoptLogCategory.POKE, url = "home/poke/friend-list-summary?type=soulmate", count = { it.soulmatesPokeCount ?: 0 });
}
