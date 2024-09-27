package org.sopt.official.domain.fortune.usecase

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class GetTodayDateUseCase @Inject constructor() {

    operator fun invoke(): String {
        val currentDate = System.currentTimeMillis()

        return SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(currentDate)
    }
}
