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
package org.sopt.official.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate

private const val BASE_DATE = "2007-03-01"

fun calculateGenerationStartDate(
    generation: Int,
    period: Int = 6,
    from: LocalDate = BASE_DATE.toLocalDate()
): LocalDate {
    val monthsToTake = generation * period
    return from.plus(monthsToTake, DateTimeUnit.MONTH)
}

fun calculateDurationOfGeneration(start: LocalDate, end: LocalDate): Int {
    val diff = start.periodUntil(end)
    return diff.months + diff.years * 12
}
