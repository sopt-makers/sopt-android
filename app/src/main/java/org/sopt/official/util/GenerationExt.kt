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