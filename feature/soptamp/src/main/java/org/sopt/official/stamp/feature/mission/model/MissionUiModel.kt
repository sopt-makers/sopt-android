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
package org.sopt.official.stamp.feature.mission.model

import org.sopt.official.domain.soptamp.MissionLevel
import org.sopt.official.domain.soptamp.model.OtherMission

data class MissionUiModel(
    val id: Int,
    val title: String,
    val level: MissionLevel,
    val isCompleted: Boolean
)

fun OtherMission.Mission.toUiModel(): MissionUiModel = MissionUiModel(
    id = this.id,
    title = this.title,
    level = MissionLevel.of(this.level),
    isCompleted = this.display,
)
