package org.sopt.official.feature.home.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.sopt.official.R
import org.sopt.official.feature.web.WebUrlConstant

enum class HomeCTAType(
    @StringRes val title: Int,
    @StringRes val description: Int?,
    val url: String?,
    @DrawableRes val icon: Int
) {
    SOPT_OFFICIAL_PAGE_URL(
        R.string.main_unauthenticated_large_block_official_page,
        null,
        WebUrlConstant.SOPT_OFFICIAL_PAGE_URL,
        R.drawable.ic_homepage_orange
    ),
    SOPT_ATTENDANCE(
        R.string.main_active_large_block_attendance,
        R.string.main_active_large_block_attendance_description,
        null,
        R.drawable.ic_attendance_orange
    ),
    SOPT_CREW_URL(
        R.string.main_inactive_large_block_crew,
        R.string.main_inactive_large_block_crew_description,
        WebUrlConstant.PLAYGROUNG_CREW_URL,
        R.drawable.ic_crew_orange
    ),
}
