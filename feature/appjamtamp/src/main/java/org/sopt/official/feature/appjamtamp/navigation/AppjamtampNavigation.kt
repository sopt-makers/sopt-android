/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.appjamtamp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import org.sopt.official.feature.appjamtamp.missiondetail.navigation.AppjamtampMissionDetail
import org.sopt.official.feature.appjamtamp.missionlist.navigation.AppjamtampMissionList
import org.sopt.official.feature.appjamtamp.ranking.navigation.AppjamtampRanking
import org.sopt.official.feature.appjamtamp.teammisisonlist.navigation.AppjamtampTeamMissionList

fun NavController.navigateToAppjamtamp(navOptions: NavOptions? = null) {
    navigate(AppjamtampNavGraph, navOptions)
}

internal fun NavController.navigateToMissionList(navOptions: NavOptions? = null) {
    navigate(AppjamtampMissionList, navOptions)
}

internal fun NavController.navigateToMissionDetail(
    missionId: Int,
    missionLevel: Int = -1,
    title: String = "",
    ownerName: String?,
    navOptions: NavOptions? = null
) {
    navigate(
        AppjamtampMissionDetail(
            missionId = missionId,
            missionLevel = missionLevel,
            title = title,
            ownerName = ownerName
        ), navOptions
    )
}

internal fun NavController.navigateToRanking(navOptions: NavOptions? = null) {
    navigate(AppjamtampRanking, navOptions)
}

internal fun NavController.navigateToTeamMissionList(
    teamNumber: String,
    navOptions: NavOptions? = null
) {
    navigate(AppjamtampTeamMissionList(teamNumber), navOptions)
}
