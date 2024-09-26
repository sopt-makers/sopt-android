package org.sopt.official.feature.fortune.feature.fortuneDetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun FortuneDetailRoute(
    paddingValue: PaddingValues,
    date: String,
    onFortuneAmuletClick: () -> Unit,
    viewModel: FortuneDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FortuneDetailScreen(
        paddingValue = paddingValue,
        date = date,
        onFortuneAmuletClick = onFortuneAmuletClick,
        uiState = uiState,
    )
}
