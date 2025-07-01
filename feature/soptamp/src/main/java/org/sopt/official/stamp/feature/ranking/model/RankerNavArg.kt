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
package org.sopt.official.stamp.feature.ranking.model

// This file might be redundant now as RankerNavArg is defined in navigation/StampNavigation.kt
// However, to minimize changes outside of navigation replacement for now,
// we'll keep this and ensure it's compatible.
// Consider removing/refactoring this file in a follow-up.

data class RankerNavArgHolder( // Renamed to avoid conflict with the one in navigation package
    val nickname: String,
    val description: String,
)

// The RankerUiModel.toRankerNavArg() or direct construction is now handled in RankingScreen.kt
// when creating UserMissionListRoute. This extension might cause confusion.
/*
fun RankerUiModel.toRankerNavArgHolder() = RankerNavArgHolder(
    nickname = this.nickname,
    description = this.getDescription()
)
*/
