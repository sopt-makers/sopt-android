package org.sopt.official.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime

fun getGeneration(generation: Int): LocalDate {
    val baseDate = "2007-03-01".toLocalDate()
    val monthsBetweenGenerations = 6
    val monthsToTake = (generation - 0) * monthsBetweenGenerations
    return baseDate.plus(monthsToTake, DateTimeUnit.MONTH)
}

fun computeMothUntilNow(generation: Int): Int {
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val generationDate = getGeneration(generation)
    val diff = generationDate.periodUntil(currentTime)
    return diff.months + diff.years * 12
}