/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.feature.home.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.sopt.official.R
import org.sopt.official.feature.web.WebUrlConstant

enum class HomeCTAType(
    @StringRes val title: Int,
    @StringRes val description: Int?,
    val url: String?,
    @DrawableRes val icon: Int,
    val clickEventName: String
) {
    SOPT_OFFICIAL_PAGE_URL(
        R.string.main_unauthenticated_large_block_official_page,
        null,
        WebUrlConstant.SOPT_OFFICIAL_PAGE_URL,
        R.drawable.ic_homepage_orange,
        "homepage"
    ),
    SOPT_ATTENDANCE(
        R.string.main_active_large_block_attendance,
        R.string.main_active_large_block_attendance_description,
        null,
        R.drawable.ic_attendance_orange,
        "attendance"
    ),
    SOPT_CREW_URL(
        R.string.main_inactive_large_block_crew,
        R.string.main_inactive_large_block_crew_description,
        WebUrlConstant.PLAYGROUNG_CREW_URL,
        R.drawable.ic_crew_orange,
        "group"
    ),
}
