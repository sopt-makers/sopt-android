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
package org.sopt.official.stamp.designsystem.component.mission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.domain.soptamp.MissionLevel
import org.sopt.official.stamp.feature.mission.model.MissionUiModel

@Composable
fun MissionComponent(
    mission: MissionUiModel,
    onClick: () -> Unit = {}
) {
    val shape = MissionShape.DEFAULT_WAVE
    val stamp = Stamp.findStampByLevel(mission.level)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(160.dp, 200.dp)
            .aspectRatio(0.8f)
            .background(
                color = if (mission.isCompleted) stamp.background else SoptTheme.colors.onSurface5,
                shape = shape
            )
            .noRippleClickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (mission.isCompleted) {
            CompletedStamp(
                stamp = stamp,
                modifier = Modifier
                    .aspectRatio(1.3f)
                    .padding(horizontal = 12.dp)
            )
        } else {
            LevelOfMission(stamp = stamp, spaceSize = 10.dp)
        }
        Spacer(modifier = Modifier.size((if (mission.isCompleted) 8 else 16).dp))
        TitleOfMission(missionTitle = mission.title)
    }
}

@Composable
private fun TitleOfMission(missionTitle: String) {
    val missionText = if (missionTitle.length > 11) {
        StringBuilder(missionTitle).insert(11, "\n")
            .toString()
    } else {
        missionTitle
    }
    Text(
        text = missionText,
        style = SoptTheme.typography.sub3,
        textAlign = TextAlign.Center,
        maxLines = 2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xff000000)
@Composable
fun PreviewMissionComponent() {
    SoptTheme {
        val previewMission = MissionUiModel(
            id = 1,
            title = "일이삼사오육칠팔구십일일이삼사오육칠팔구십일",
            level = MissionLevel.of(1),
            isCompleted = true
        )
        MissionComponent(
            mission = previewMission
        )
    }
}
