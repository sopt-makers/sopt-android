package org.sopt.official.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate

const val BASE_DATE = "2007-03-01"

fun getGenerationStartDate(generation: Int, baseLocalDate: LocalDate = BASE_DATE.toLocalDate(), monthsBetweenGenerations: Int = 6): LocalDate {
    val monthsToTake = generation * monthsBetweenGenerations
    return baseLocalDate.plus(monthsToTake, DateTimeUnit.MONTH)
}

fun computeMothUntilNow(generation: Int, currentTime: LocalDate): Int {
    val generationStartDate = getGenerationStartDate(generation)
    val diff = generationStartDate.periodUntil(currentTime)
    return diff.months + diff.years * 12
}