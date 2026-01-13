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

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.MainTabRoute
import org.sopt.official.feature.appjamtamp.missiondetail.MissionDetailRoute
import org.sopt.official.feature.appjamtamp.missiondetail.navigation.AppjamtampMissionDetail
import org.sopt.official.feature.appjamtamp.missionlist.AppjamtampMissionRoute
import org.sopt.official.feature.appjamtamp.missionlist.navigation.AppjamtampMissionList
import org.sopt.official.feature.appjamtamp.ranking.AppjamtampRankingRoute
import org.sopt.official.feature.appjamtamp.ranking.navigation.AppjamtampRanking
import org.sopt.official.feature.appjamtamp.teammisisonlist.AppjamtampTeamMissionListRoute
import org.sopt.official.feature.appjamtamp.teammisisonlist.navigation.AppjamtampTeamMissionList

@Serializable
data object AppjamtampNavGraph : MainTabRoute

fun NavGraphBuilder.appjamtampNavGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {
    navigation<AppjamtampNavGraph>(
        startDestination = AppjamtampMissionList
    ) {
        composable<AppjamtampMissionList> {
            AppjamtampMissionRoute(
                navigateToMissionDetail = navController::navigateToMissionDetail,
                navigateToRanking = navController::navigateToRanking
            )
        }

        composable<AppjamtampMissionDetail> {
            MissionDetailRoute(
                navigateUp = navController::navigateUp,
            )
        }

        composable<AppjamtampRanking> {
            AppjamtampRankingRoute(
                navigateUp = navController::navigateUp,
                navigateToMissionDetail = { missionId, ownerName, teamNumber ->
                    navController.navigateToTeamMissionList(teamNumber)
                    navController.navigateToMissionDetail(
                        missionId = missionId,
                        ownerName = ownerName
                    )
                },
                navigateToTeamMissionList = navController::navigateToTeamMissionList
            )
        }

        composable<AppjamtampTeamMissionList> { backStackEntry ->
            val args = backStackEntry.toRoute<AppjamtampTeamMissionList>()

            AppjamtampTeamMissionListRoute(
                teamNumber = args.teamNumber,
                navigateUp = navController::navigateUp,
                navigateToMissionDetail = navController::navigateToMissionDetail
            )
        }
    }
}
