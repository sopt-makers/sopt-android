/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.domain.poke.type

enum class PokeFriendType(
    val typeName: String,
    val readableName: String,
    val title: String,
    val description: String
) {
    NEW(
        typeName = "new",
        readableName = "친한친구",
        title = "나랑 친한 친구",
        description = "2번 이상 찌르면 될 수 있어요"
    ),
    BEST_FRIEND(
        typeName = "bestfriend",
        readableName = "단짝친구",
        title = "나랑 단짝친구",
        description = "5번 이상 찌르면 될 수 있어요"
    ),
    SOULMATE(
        typeName = "soulmate",
        readableName = "천생연분",
        title = "나랑 천생연분",
        description = "11번 이상 찌르면 될 수 있어요"
    )
}
