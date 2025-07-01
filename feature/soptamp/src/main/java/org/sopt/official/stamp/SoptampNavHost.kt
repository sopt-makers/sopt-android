package org.sopt.official.stamp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import org.sopt.official.stamp.feature.mission.detail.MissionDetailScreen
import org.sopt.official.stamp.feature.mission.list.MissionListScreen
import org.sopt.official.stamp.feature.mission.list.UserMissionListScreen
import org.sopt.official.stamp.feature.onboarding.OnboardingScreen
import org.sopt.official.stamp.feature.ranking.part.PartRankingScreen
import org.sopt.official.stamp.feature.ranking.rank.RankingScreen
import org.sopt.official.stamp.navigation.MissionDetailRoute
import org.sopt.official.stamp.navigation.MissionListRoute
import org.sopt.official.stamp.navigation.MissionNavArgs
import org.sopt.official.stamp.navigation.OnboardingRoute
import org.sopt.official.stamp.navigation.PartRankingRoute
import org.sopt.official.stamp.navigation.RankerNavArg
import org.sopt.official.stamp.navigation.RankingNavArg
import org.sopt.official.stamp.navigation.RankingRoute
import org.sopt.official.stamp.navigation.UserMissionListRoute

const val SOPTAMP_NAV_HOST_ROUTE = "soptamp_nav_host_route"
private const val DEEP_LINK_SCHEME = "soptamp"

@Composable
fun SoptampNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onShowErrorToast: (throwable: Throwable) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MissionListRoute,
        modifier = modifier,
        route = SOPTAMP_NAV_HOST_ROUTE
    ) {
        composable<MissionListRoute> {
            MissionListScreen(
                navigator = navController,
                onShowErrorToast = onShowErrorToast
            )
        }
        composable<MissionDetailRoute>(
            deepLinks = listOf(
                navDeepLink<MissionDetailRoute>(
                    basePath = "$DEEP_LINK_SCHEME://mission/detail/{missionId}/{missionTitle}/{missionLevel}/{isCompleted}/{isMe}/{nickname}",
                    action = "android.intent.action.VIEW"
                )
            )
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<MissionDetailRoute>()
            MissionDetailScreen(
                args = MissionNavArgs.from(args),
                resultNavigator = navController
            )
        }
        composable<UserMissionListRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<UserMissionListRoute>()
            UserMissionListScreen(
                args = RankerNavArg.from(args),
                resultNavigator = navController,
                navigator = navController
            )
        }
        composable<OnboardingRoute> {
            OnboardingScreen(navigator = navController)
        }
        composable<PartRankingRoute> {
            PartRankingScreen(
                resultNavigator = navController,
                navigator = navController
            )
        }
        composable<RankingRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<RankingRoute>()
            RankingScreen(
                args = RankingNavArg.from(args),
                resultNavigator = navController,
                navigator = navController
            )
        }
    }
}

// Extension for NavController to navigate back with a result
fun NavHostController.navigateBackWithResult(key: String, value: Any) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
    popBackStack()
}

// Extension for NavController to get a result from the previous screen
@Composable
fun <T> NavHostController.getResult(key: String, onResult: (T) -> Unit) {
    currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)?.observeForever {
        onResult(it)
        currentBackStackEntry?.savedStateHandle?.remove<T>(key) // consume the result
    }
}
