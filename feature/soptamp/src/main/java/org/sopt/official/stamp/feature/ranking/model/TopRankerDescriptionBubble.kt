/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.ranking.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.Mint300
import org.sopt.official.stamp.designsystem.style.Pink300
import org.sopt.official.stamp.designsystem.style.Purple300

enum class TopRankerDescriptionBubble(
    private val rank: Int,
    @DrawableRes val background: Int,
    val backgroundColor: Color,
    val textColor: Color,
) {
    FIRST(
        rank = 1,
        background = R.drawable.bubble_middle,
        backgroundColor = Purple300,
        textColor = Color.White
    ),
    SECOND(
        rank = 2,
        background = R.drawable.bubble_left,
        backgroundColor = Pink300,
        textColor = Color.White
    ),
    THIRD(
        rank = 3,
        background = R.drawable.bubble_right,
        backgroundColor = Mint300,
        textColor = Color.Black
    );

    fun hasRankOf(rank: Int): Boolean {
        return this.rank == rank
    }

    companion object {
        fun findBubbleByRank(rank: Int): TopRankerDescriptionBubble = entries.find { it.hasRankOf(rank) }
            ?: throw IllegalStateException("$rank 는 상위 3위에 포함된 랭킹이 아닙니다.")
    }
}
