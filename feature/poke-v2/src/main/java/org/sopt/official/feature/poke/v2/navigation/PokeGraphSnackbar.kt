/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.v2.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy

/**
 * [PokeGraph] 스코프 기반의 공유 [SnackbarHostState]를 반환하는 헬퍼 함수.
 *
 * - **PokeGraph 내부**: 백스택의 [PokeSnackbarViewModel] 인스턴스를 공유.
 * - **PokeGraph 외부**: [NavDestination.hierarchy] 체크 후 안전하게 로컬 [SnackbarHostState]로 폴백.
 */
@Composable
fun rememberPokeGraphSnackbarHostState(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
): SnackbarHostState {
    val pokeGraphEntry = remember(backStackEntry) {
        val isInsidePokeGraph = backStackEntry.destination.hierarchy.any { it.hasRoute<PokeGraph>() }
        if (isInsidePokeGraph) navController.getBackStackEntry(PokeGraph) else null
    }

    return if (pokeGraphEntry != null) {
        hiltViewModel<PokeSnackbarViewModel>(pokeGraphEntry).snackbarHostState
    } else {
        remember { SnackbarHostState() }
    }
}
