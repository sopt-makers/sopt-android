/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.designsystem.component.mission

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.sopt.official.domain.soptamp.MissionLevel
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.SoptTheme

enum class Stamp(
    val missionLevel: MissionLevel,
    @DrawableRes val lottieImage: Int
) {
    LEVEL1(MissionLevel.of(1), R.drawable.pinkstamp_image),
    LEVEL2(MissionLevel.of(2), R.drawable.purplestamp_image),
    LEVEL3(MissionLevel.of(3), R.drawable.greenstamp_image);

    val starColor: Color
        @Composable get() = when (this) {
            LEVEL1 -> SoptTheme.colors.pink300
            LEVEL2 -> SoptTheme.colors.purple300
            LEVEL3 -> SoptTheme.colors.mint300
        }

    val background: Color
        @Composable get() = when (this) {
            LEVEL1 -> SoptTheme.colors.pink100
            LEVEL2 -> SoptTheme.colors.purple100
            LEVEL3 -> SoptTheme.colors.mint100
        }

    fun hasStampLevel(level: MissionLevel): Boolean {
        return this.missionLevel == level
    }

    companion object {
        val defaultStarColor: Color
            @Composable get() = SoptTheme.colors.onSurface30

        fun findStampByLevel(level: MissionLevel): Stamp = entries.find {
            it.hasStampLevel(level)
        } ?: throw IllegalArgumentException("$level 에 해당하는 Stamp 가 없습니다.")
    }
}
