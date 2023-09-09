/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    val textColor: Color
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
