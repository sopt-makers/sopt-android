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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.stamp.designsystem.component.util.noRippleClickable
import org.sopt.official.stamp.designsystem.style.SoptTheme
import org.sopt.official.stamp.domain.MissionLevel
import org.sopt.official.stamp.feature.mission.model.MissionUiModel

@Composable
fun MissionComponent(
    mission: MissionUiModel,
    onClick: (() -> Unit) = {}
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
                modifier = Modifier.aspectRatio(1.3f)
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
