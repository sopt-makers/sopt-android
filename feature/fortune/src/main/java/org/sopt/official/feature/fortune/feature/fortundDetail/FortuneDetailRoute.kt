package org.sopt.official.feature.fortune.feature.fortundDetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
internal fun FortuneDetailRoute(
    paddingValue: PaddingValues,
    date: String,
    navigateToFortuneAmulet: () -> Unit,
) {
    FortuneDetailScreen(
        paddingValue = paddingValue,
        date = date,
        navigateToFortuneAmulet = navigateToFortuneAmulet
    )
}
