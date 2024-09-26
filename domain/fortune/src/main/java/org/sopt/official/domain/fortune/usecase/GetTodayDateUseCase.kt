package org.sopt.official.domain.fortune.usecase

import java.time.LocalDate
import javax.inject.Inject

class GetTodayDateUseCase @Inject constructor() {
    private val currentDate by lazy { LocalDate.now() }

    operator fun invoke(): String = currentDate.toFormattedDate()

    private fun LocalDate.toFormattedDate(): String {
        val month = monthValue.toString().padStart(2, '0')
        val day = dayOfMonth.toString().padStart(2, '0')

        return "$year-$month-$day"
    }
}
