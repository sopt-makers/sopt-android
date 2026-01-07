package org.sopt.official.feature.appjamtamp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
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
                paddingValues = paddingValues
            )
        }

        composable<AppjamtampMissionDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<AppjamtampMissionDetail>()
            MissionDetailRoute(
                navigateUp = navController::navigateUp,
            )
        }

        composable<AppjamtampRanking> { backStackEntry ->
            val args = backStackEntry.toRoute<AppjamtampRanking>()
            AppjamtampRankingRoute()
        }

        composable<AppjamtampTeamMissionList> {
            AppjamtampTeamMissionListRoute()
        }
    }
}

fun NavController.navigateToAppjamtamp(navOptions: NavOptions? = null) {
    navigate(AppjamtampNavGraph, navOptions)
}
