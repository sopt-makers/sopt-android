package org.sopt.official.feature.fortune.feature.fortuneDetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.sopt.official.designsystem.Gray800
import org.sopt.official.feature.fortune.feature.fortuneDetail.component.PokeMessageBottomSheetScreen

@Composable
internal fun FortuneDetailRoute(
    date: String,
    onFortuneAmuletClick: () -> Unit,
    viewModel: FortuneDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isAnonymous by remember { mutableStateOf(true) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
        ),
        sheetBackgroundColor = Gray800,
        sheetContent = {
            PokeMessageBottomSheetScreen(
                selectedIndex = selectedIndex,
                onItemClick = { newSelectedIndex, message ->
                    scope.launch {
                        selectedIndex = newSelectedIndex
                        bottomSheetState.hide()
                        viewModel.poke(message)
                    }
                },
                onIconClick = { isAnonymous = !isAnonymous },
                isAnonymous = isAnonymous,
            )
        },
    ) {
        FortuneDetailScreen(
            date = date,
            onFortuneAmuletClick = onFortuneAmuletClick,
            onProfileClick = { userId ->
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://playground.sopt.org/members/${userId}"))
                )
            },
            onPokeClick = { scope.launch { bottomSheetState.show() } },
            uiState = uiState,
        )
    }
}
