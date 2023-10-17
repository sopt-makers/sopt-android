/*
 * MIT License
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.update

import com.google.android.play.core.install.model.AppUpdateType

enum class UpdateCriteria(
    val priority: Int,
    val stalenessDaysOfImmediateUpdate: Int?,
    val stalenessDaysOfFlexibleUpdate: Int?
) {
    URGENT(5, null, null), SLIGHTLY_URGENT(4, 5, 3), NORMAL(3, 30, 15), SLIGHTLY_FLEXIBLE(2, 90, 30), FLEXIBLE(1, null, null);

    companion object {
        private fun of(priority: Int): UpdateCriteria {
            return entries.find { it.priority == priority } ?: throw IllegalStateException("Unknown priority: $priority")
        }

        private fun UpdateCriteria.stalenessDayOf(type: Int) = if (type == AppUpdateType.IMMEDIATE) {
            stalenessDaysOfImmediateUpdate!!
        } else {
            stalenessDaysOfFlexibleUpdate!!
        }

        fun isUpdatableOf(
            priority: Int,
            stalenessDays: Int?,
            updateType: Int
        ): Boolean {
            return when (priority) {
                5 -> true
                1 -> updateType == AppUpdateType.FLEXIBLE
                else -> {
                    if (stalenessDays == null) return false
                    val criteria = UpdateCriteria.of(priority)
                    stalenessDays >= criteria.stalenessDayOf(AppUpdateType.IMMEDIATE)
                }
            }
        }
    }
}
