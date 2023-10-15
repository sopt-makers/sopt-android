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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import org.sopt.official.stamp.R
import org.sopt.official.domain.soptamp.MissionLevel

@Composable
fun LevelOfMission(stamp: Stamp, spaceSize: Dp) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spaceSize)
    ) {
        MissionLevelOfStar(stamp = stamp)
    }
}

@Composable
private fun MissionLevelOfStar(stamp: Stamp) {
    (MissionLevel.MINIMUM_LEVEL..MissionLevel.MAXIMUM_LEVEL).forEach {
        val starColor = if (it <= stamp.missionLevel.value) {
            stamp.starColor
        } else {
            Stamp.defaultStarColor
        }
        Icon(
            painter = painterResource(id = R.drawable.level_star),
            contentDescription = "Star Of Mission Level",
            tint = starColor
        )
    }
}
