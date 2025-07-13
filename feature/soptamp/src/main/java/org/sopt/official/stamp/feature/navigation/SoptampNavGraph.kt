/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

fun NavGraphBuilder.soptampNavGraph(
    navController: NavController,
    onBackClick: () -> Unit = { navController.popBackStack() },
) {
    composable<MissionList> {
        MissionListScreenRoute(navController)
    }

    composable<MissionDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<MissionDetail>()
        MissionDetailScreenRoute(args, navController)
    }

    composable<Ranking> { backStackEntry ->
        val args = backStackEntry.toRoute<Ranking>()
        RankingScreenRoute(args, navController)
    }

    composable<PartRanking> {
        PartRankingScreenRoute(navController)
    }

    composable<UserMissionList> { backStackEntry ->
        val args = backStackEntry.toRoute<UserMissionList>()
        UserMissionListScreenRoute(args, navController)
    }

    composable<Onboarding> {
        OnboardingScreenRoute(navController)
    }
}
