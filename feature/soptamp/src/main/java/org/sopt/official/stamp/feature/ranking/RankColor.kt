/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.feature.ranking

import androidx.compose.runtime.Composable
import org.sopt.official.stamp.designsystem.style.SoptTheme

@Composable
fun getRankTextColor(rank: Int) = when (rank) {
    1 -> SoptTheme.colors.purple300
    2 -> SoptTheme.colors.pink300
    3 -> SoptTheme.colors.mint300
    else -> SoptTheme.colors.onSurface50
}

@Composable
fun getLevelTextColor(rank: Int) = when (rank) {
    1 -> SoptTheme.colors.pink300
    2 -> SoptTheme.colors.purple300
    3 -> SoptTheme.colors.mint300
    else -> SoptTheme.colors.onSurface50
}

@Composable
fun getLevelBackgroundColor(rank: Int) = when (rank) {
    1 -> SoptTheme.colors.pink200
    2 -> SoptTheme.colors.purple200
    3 -> SoptTheme.colors.mint200
    else -> SoptTheme.colors.onSurface5
}

@Composable
fun getRankBackgroundColor(rank: Int) = when (rank) {
    1 -> SoptTheme.colors.purple200
    2 -> SoptTheme.colors.pink200
    3 -> SoptTheme.colors.mint200
    else -> SoptTheme.colors.onSurface5
}

@Composable
fun headerBackgroundColorOf(rank: Int) = when (rank) {
    1 -> SoptTheme.colors.pink100
    2 -> SoptTheme.colors.purple100
    3 -> SoptTheme.colors.mint100
    else -> SoptTheme.colors.onSurface5
}
