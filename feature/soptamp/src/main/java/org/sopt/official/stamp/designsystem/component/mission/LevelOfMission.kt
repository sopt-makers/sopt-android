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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.Gray600
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.domain.soptamp.MissionLevel
import org.sopt.official.stamp.R
import org.sopt.official.stamp.designsystem.style.Orange300

@Composable
fun LevelOfMission(stamp: Stamp, spaceSize: Dp) {
    if (stamp.missionLevel == MissionLevel.of(10)) {
        SpecialMission(stamp = stamp)
    } else Row(
        horizontalArrangement = Arrangement.spacedBy(spaceSize)
    ) {
        MissionLevelOfStar(stamp = stamp)
    }
}

@Composable
private fun SpecialMission(stamp: Stamp) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.level_star),
            contentDescription = "Star Of Mission Level",
            tint = stamp.starColor
        )
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_text_close),
            contentDescription = null,
            tint = White
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "10",
            style = SoptTheme.typography.body14M,
            color = White
        )
        Spacer(modifier = Modifier.width(7.dp))
        VerticalDivider(
            thickness = 1.dp,
            modifier = Modifier.height(7.dp),
            color = Gray600
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = "특별미션",
            style = SoptTheme.typography.body14M,
            color = Orange300
        )
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
