/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.feature.ranking

import androidx.compose.runtime.Composable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.stamp.designsystem.style.Mint300
import org.sopt.official.stamp.designsystem.style.Pink300
import org.sopt.official.stamp.designsystem.style.Purple300

@Composable
fun getRankTextColor(rank: Int) =
    when (rank) {
        1 -> Pink300
        2 -> Mint300
        3 -> Purple300
        else -> SoptTheme.colors.onSurface300
    }

@Composable
fun getRankBackgroundColor(rank: Int) =
    when (rank) {
        1 -> Pink300
        2 -> Mint300
        3 -> Purple300
        else -> SoptTheme.colors.onSurface700
    }

@Composable
fun getStarColor(rank: Int) =
    when (rank) {
        1 -> Pink300
        2 -> Purple300
        3 -> Mint300
        else -> SoptTheme.colors.onSurface700
    }
