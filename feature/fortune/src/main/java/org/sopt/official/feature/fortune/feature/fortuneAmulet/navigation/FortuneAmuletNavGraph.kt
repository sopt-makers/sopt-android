package org.sopt.official.feature.fortune.feature.fortuneAmulet.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.sopt.official.feature.fortune.feature.fortuneAmulet.FortuneAmuletRoute

@Serializable
data object FortuneAmulet

fun NavGraphBuilder.fortuneAmuletNavGraph(
    paddingValue: PaddingValues,
    navigateToHome: () -> Unit,
) {
    composable<FortuneAmulet> {
        FortuneAmuletRoute(
            paddingValue = paddingValue,
            navigateToHome = navigateToHome
        )
    }
}
