package org.sopt.official.feature.auth.feature.certificate

import java.util.Locale

data class CertificationState(
    val phone: String = "",
    val code: String = "",
    val currentTimeValue: Int = 180
) {
    val currentTime: String
        get() = String.format(
            Locale.US,
            "%02d:%02d",
            currentTimeValue / 60,
            currentTimeValue % 60
        )
}