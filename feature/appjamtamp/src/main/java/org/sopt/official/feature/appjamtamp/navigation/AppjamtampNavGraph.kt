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
