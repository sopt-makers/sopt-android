/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
