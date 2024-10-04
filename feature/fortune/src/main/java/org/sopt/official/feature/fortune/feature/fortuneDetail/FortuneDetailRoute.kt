package org.sopt.official.feature.fortune.feature.fortuneDetail

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.sopt.official.feature.fortune.feature.fortuneDetail.component.PokeMessageBottomSheetScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FortuneDetailRoute(
    date: String,
    onFortuneAmuletClick: () -> Unit,
    viewModel: FortuneDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isAnonymous by remember { mutableStateOf(true) }
    var isShowBottomSheet by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val bottomSheetState = rememberModalBottomSheetState(confirmValueChange = { false })
    val scope = rememberCoroutineScope()

    if (isShowBottomSheet) {
        PokeMessageBottomSheetScreen(
            sheetState = bottomSheetState,
            onDismissRequest = { isShowBottomSheet = false },
            selectedIndex = selectedIndex,
            onItemClick = { newSelectedIndex, message ->
                scope.launch {
                    selectedIndex = newSelectedIndex
                    viewModel.poke(message)
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) isShowBottomSheet = false
                }
            },
            onIconClick = { isAnonymous = !isAnonymous },
            isAnonymous = isAnonymous,
        )
    }

    FortuneDetailScreen(
        date = date,
        onFortuneAmuletClick = onFortuneAmuletClick,
        onProfileClick = { userId ->
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://playground.sopt.org/members/${userId}"))
            )
        },
        onPokeClick = { isShowBottomSheet = true },
        uiState = uiState,
    )
}
