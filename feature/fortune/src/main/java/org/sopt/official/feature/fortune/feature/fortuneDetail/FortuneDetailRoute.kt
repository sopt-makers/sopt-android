package org.sopt.official.feature.fortune.feature.fortuneDetail

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.feature.fortune.feature.fortuneDetail.component.PokeMessageBottomSheetScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FortuneDetailRoute(
    paddingValue: PaddingValues,
    date: String,
    onFortuneAmuletClick: () -> Unit,
    viewModel: FortuneDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isSelected by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val bottomSheetState = rememberModalBottomSheetState(confirmValueChange = { false })

    if (showBottomSheet) {
        PokeMessageBottomSheetScreen(
            sheetState = bottomSheetState,
            onDismissRequest = {},
            isSelected = isSelected,
            selectedIndex = selectedIndex,
            onItemClick = { newSelectedIndex ->
                selectedIndex = newSelectedIndex
            },
            onIconClick = { isSelected = !isSelected },
        )
    }

    FortuneDetailScreen(
        paddingValue = paddingValue,
        date = date,
        onFortuneAmuletClick = onFortuneAmuletClick,
        onPokeClick = { showBottomSheet = !showBottomSheet },
        uiState = uiState,
    )
}
