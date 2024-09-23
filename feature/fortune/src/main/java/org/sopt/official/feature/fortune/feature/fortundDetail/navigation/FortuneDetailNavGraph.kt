package org.sopt.official.feature.fortune.feature.fortundDetail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.sopt.official.feature.fortune.feature.fortundDetail.FortuneDetailRoute

@Serializable
data class FortuneDetail(val date: String)

fun NavGraphBuilder.fortuneDetailNavGraph(
    paddingValue: PaddingValues,
    navigateToFortuneAmulet: () -> Unit,
) {
    composable<FortuneDetail> { backStackEntry ->
        val items = backStackEntry.toRoute<FortuneDetail>()
        FortuneDetailRoute(
            paddingValue = paddingValue,
            date = items.date,
            navigateToFortuneAmulet = navigateToFortuneAmulet
        )
    }
}
