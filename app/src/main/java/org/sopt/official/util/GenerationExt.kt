package org.sopt.official.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd")
fun getGeneration(generation: Int): LocalDate =
        java.time.LocalDate.parse(
                when (generation) {
                    32 -> "2023.03.01"
                    31 -> "2022.09.01"
                    30 -> "2022.03.01"
                    29 -> "2021.09.01"
                    28 -> "2021.03.01"
                    27 -> "2020.09.01"
                    26 -> "2020.03.01"
                    25 -> "2019.09.01"
                    24 -> "2019.03.01"
                    23 -> "2018.09.01"
                    22 -> "2018.03.01"
                    21 -> "2017.09.01"
                    else -> "2017.03.01"
                },
                DATE_FORMAT
        ).toKotlinLocalDate()

fun computeMothUntilNow(generation: Int): Int {
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val generationDate = getGeneration(generation)
    val diff = generationDate.periodUntil(currentTime)
    return diff.months + diff.years * 12
}