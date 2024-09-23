package org.sopt.official.feature.fortune.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.sopt.official.feature.fortune.feature.fortuneAmulet.navigation.FortuneAmulet
import org.sopt.official.feature.fortune.feature.home.HomeRoute

@Serializable
data object Home

fun NavGraphBuilder.homeNavGraph(
    paddingValue: PaddingValues,
    navigateToFortuneDetail: (String) -> Unit,
) {
    composable<FortuneAmulet> {
        HomeRoute(
            paddingValue = paddingValue,
            navigateToFortuneDetail = navigateToFortuneDetail
        )
    }
}
