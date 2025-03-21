/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.fortune.feature.fortuneDetail.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.sopt.official.feature.fortune.feature.fortuneDetail.FortuneDetailRoute
import org.sopt.official.feature.fortune.feature.fortuneDetail.model.SnackBarUiState

@Serializable
data class FortuneDetail(val date: String)

fun NavGraphBuilder.fortuneDetailNavGraph(
    navigateToFortuneAmulet: () -> Unit,
    snackBarHostState: SnackbarHostState,
    isBottomSheetVisible: (isVisible: Boolean) -> Unit,
    showSnackBar: (case: SnackBarUiState) -> Unit,
) {
    composable<FortuneDetail> { backStackEntry ->
        val items = backStackEntry.toRoute<FortuneDetail>()
        FortuneDetailRoute(
            date = items.date,
            onFortuneAmuletClick = navigateToFortuneAmulet,
            isBottomSheetVisible = isBottomSheetVisible,
            snackBarHostState = snackBarHostState,
            showSnackBar = showSnackBar,
        )
    }
}
