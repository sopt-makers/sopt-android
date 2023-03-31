/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.designsystem.component.mission

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.domain.MissionLevel

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

        fun findStampByLevel(level: MissionLevel): Stamp = values().find {
            it.hasStampLevel(level)
        } ?: throw IllegalArgumentException("$level 에 해당하는 Stamp 가 없습니다.")
    }
}
