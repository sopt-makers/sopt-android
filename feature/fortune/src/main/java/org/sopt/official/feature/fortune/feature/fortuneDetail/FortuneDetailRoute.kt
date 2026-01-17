/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.fortune.feature.fortuneDetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.zacsweers.metro.viewmodel.compose.metroViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.sopt.official.analytics.EventType.CLICK
import org.sopt.official.analytics.EventType.VIEW
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.FortuneDetailUiState.Success
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.SnackBarUiState

internal const val DEFAULT_ID = -1

@Composable
internal fun FortuneDetailRoute(
    date: String,
    onFortuneAmuletClick: () -> Unit,
    isBottomSheetVisible: (isVisible: Boolean) -> Unit,
    snackBarHostState: SnackbarHostState,
    showSnackBar: (case: SnackBarUiState) -> Unit,
    viewModel: FortuneDetailViewModel = metroViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isPokeEnabled by remember { mutableStateOf(true) }
    // var isAnonymous by remember { mutableStateOf(true) }
    // var selectedIndex by remember { mutableIntStateOf(DEFAULT_ID) }
    val bottomSheetState = rememberModalBottomSheetState(initialValue = Hidden)
    val scope = rememberCoroutineScope()
    val amplitudeTracker = LocalTracker.current

    LaunchedEffect(key1 = uiState) {
        if (uiState is Success) {
            amplitudeTracker.track(
                type = VIEW,
                name = "soptmadi_todays",
            )
        }
    }

    /*LaunchedEffect(key1 = bottomSheetState.currentValue) {
        if (bottomSheetState.currentValue == Hidden) isBottomSheetVisible(false)
    }*/

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
        ),
        sheetBackgroundColor = colors.onSurface800,
        sheetContent = {
            /*PokeMessageBottomSheetScreen(
                selectedIndex = selectedIndex,
                onItemClick = { newSelectedIndex, message ->
                    amplitudeTracker.track(
                        type = CLICK,
                        name = "send_choice",
                        properties = mapOf(
                            "index" to newSelectedIndex + 1,
                            "message" to message,
                        ),
                    )
                    scope.launch {
                        selectedIndex = newSelectedIndex
                        isPokeEnabled = false
                        bottomSheetState.hide()
                        viewModel.poke(message)
                        showSnackBar(Poke)
                        snackBarHostState.showSnackbar(
                            message = "",
                            duration = Short,
                        )
                    }.invokeOnCompletion {
                        isBottomSheetVisible(false)
                    }
                },
                onIconClick = {
                    isAnonymous = !isAnonymous
                    amplitudeTracker.track(
                        type = CLICK,
                        name = "anonymity",
                        properties = mapOf("isAnonymous" to isAnonymous),
                    )
                    if (isAnonymous.not()) scope.launch {
                        showSnackBar(Anonymous)
                        snackBarHostState.showSnackbar(
                            message = "",
                            duration = Short,
                        )
                    }
                },
                isAnonymous = isAnonymous,
            )*/
        },
    ) {
        FortuneDetailScreen(
            date = date,
            onFortuneAmuletClick = {
                amplitudeTracker.track(
                    type = CLICK,
                    name = "get_charmcard",
                )
                onFortuneAmuletClick()
            },
            onProfileClick = { userId ->
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://playground.sopt.org/members/${userId}"))
                )
            },
            onPokeClick = {
                amplitudeTracker.track(
                    type = CLICK,
                    name = "randomepeople",
                )
                scope.launch {
                    bottomSheetState.show()
                }.invokeOnCompletion {
                    isBottomSheetVisible(true)
                }
            },
            onErrorDialogCheckClick = viewModel::updateUi,
            uiState = uiState,
            isEnabled = isPokeEnabled,
        )
    }
}
