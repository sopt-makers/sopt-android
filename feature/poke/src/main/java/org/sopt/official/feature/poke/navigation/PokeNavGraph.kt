package org.sopt.official.feature.poke.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sopt.official.feature.poke.bridge.PokeEntryRoute
import org.sopt.official.feature.poke.friend.summary.FriendListSummaryScreen
import org.sopt.official.feature.poke.main.PokeScreen
import org.sopt.official.feature.poke.notification.PokeNotificationScreen
import org.sopt.official.feature.poke.onboarding.OnboardingScreen
import org.sopt.official.model.UserStatus

fun NavGraphBuilder.pokeNavGraph(
    navController: NavController,
    userStatus: UserStatus,
    paddingValues: PaddingValues,
) {
    navigation<PokeGraph> (
        startDestination = PokeEntry
    ) {
        // 브릿지 화면 - 로딩
        composable<PokeEntry> {
            PokeEntryRoute(
                paddingValues = paddingValues,
                navController = navController,
                userStatus = userStatus
            )
        }

        // 콕 찌르기 메인 화면
        composable<PokeMain> {
            PokeScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
                navigateToPokeNotification = {
                    navController.navigate(PokeNotification(userStatus.name))
                }
            )
        }

        // 온보딩 화면
        composable<PokeOnboarding> {
            OnboardingScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
                navigateToPokeMain = {
                    navController.navigate(PokeMain) {
                        popUpTo<PokeOnboarding> {
                            inclusive = true
                        }
                    }
                },
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }

        // 알림 화면
        composable<PokeNotification> {
            PokeNotificationScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
            )
        }

        // 친한 친구 리스트
        composable<PokeFriendList> {
            FriendListSummaryScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
            )
        }
    }
}