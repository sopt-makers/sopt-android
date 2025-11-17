/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.sopt.official.domain.soptamp.MissionLevel
import org.sopt.official.stamp.feature.mission.model.MissionNavArgs
import org.sopt.official.stamp.feature.ranking.model.RankerNavArg

// Result navigation keys
const val MISSION_DETAIL_RESULT_KEY = "mission_detail_result"

// Navigation functions
fun NavController.navigateToMissionList(navOptions: NavOptions? = null) {
    navigate(MissionList, navOptions)
}

fun NavController.navigateToMissionDetail(
    id: Int,
    title: String,
    level: MissionLevel,
    isCompleted: Boolean,
    isMe: Boolean,
    nickname: String,
    myName: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        MissionDetail(
            id = id,
            title = title,
            level = level.value,
            isCompleted = isCompleted,
            isMe = isMe,
            nickname = nickname,
            myName = myName,
        ),
        navOptions,
    )
}

fun NavController.navigateToMissionDetail(
    missionNavArgs: MissionNavArgs,
    navOptions: NavOptions? = null,
) {
    navigate(missionNavArgs.toRoute(), navOptions)
}

fun NavController.navigateToRanking(
    type: String,
    entrySource: String,
    navOptions: NavOptions? = null,
) {
    navigate(Ranking(type, entrySource), navOptions)
}

fun NavController.navigateToPartRanking(navOptions: NavOptions? = null) {
    navigate(PartRanking, navOptions)
}

fun NavController.navigateToUserMissionList(
    rankerNavArg: RankerNavArg,
    navOptions: NavOptions? = null,
) {
    navigate(rankerNavArg.toRoute(), navOptions)
}

fun NavController.navigateToUserMissionList(
    nickname: String,
    description: String,
    entrySource: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        UserMissionList(
            nickname = nickname,
            description = description,
            entrySource = entrySource,
        ),
        navOptions,
    )
}

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    navigate(Onboarding, navOptions)
}

// Result navigation functions
fun NavController.setMissionDetailResult(result: Boolean) {
    previousBackStackEntry?.savedStateHandle?.set(MISSION_DETAIL_RESULT_KEY, result)
}

fun NavController.getMissionDetailResult(): StateFlow<Boolean?> {
    return currentBackStackEntry?.savedStateHandle
        ?.getStateFlow(MISSION_DETAIL_RESULT_KEY, null)
        ?: MutableStateFlow(null)
}

fun NavController.clearMissionDetailResult() {
    currentBackStackEntry?.savedStateHandle?.remove<Boolean>(MISSION_DETAIL_RESULT_KEY)
}
