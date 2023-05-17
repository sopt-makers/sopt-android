package org.sopt.official.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime

const val BASE_DATE = "2007-03-01"

fun getGeneration(generation: Int): LocalDate {
    val baseLocalDate = BASE_DATE.toLocalDate()
    val monthsBetweenGenerations = 6
    val monthsToTake = generation * monthsBetweenGenerations
    return baseLocalDate.plus(monthsToTake, DateTimeUnit.MONTH)
}

fun computeMothUntilNow(generation: Int): Int {
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val generationDate = getGeneration(generation)
    val diff = generationDate.periodUntil(currentTime)
    return diff.months + diff.years * 12
}