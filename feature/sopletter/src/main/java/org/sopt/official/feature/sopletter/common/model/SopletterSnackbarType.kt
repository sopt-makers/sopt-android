package org.sopt.official.feature.sopletter.common.model

import androidx.annotation.DrawableRes
import org.sopt.official.sopletter.R

enum class SopletterSnackbarType(
    @param:DrawableRes val iconRes: Int,
) {
    SUCCESS(R.drawable.ic_alert_circle_success_20),
    WARNING(R.drawable.ic_alert_circle_warning_20),
    FAILURE(R.drawable.ic_alert_circle_failure_20),
}