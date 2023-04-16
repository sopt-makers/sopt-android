package org.sopt.official.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date

private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.mm.dd")
fun getGeneration(generation: Int): LocalDate =
    LocalDateTime.parse(
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
    ).toLocalDate()

fun computeMothUntilNow(generation: Int): Int {
    val currentTime = LocalDateTime.now().toLocalDate()
    val generationDate = getGeneration(generation)

    val diff = Period.between(generationDate, currentTime)
    return diff.months + diff.years * 12
}