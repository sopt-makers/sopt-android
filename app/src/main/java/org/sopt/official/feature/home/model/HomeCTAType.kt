/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.home.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.sopt.official.R
import org.sopt.official.feature.mypage.web.WebUrlConstant

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
    SOPT_PLAYGROUND_URL(
        R.string.main_inactive_large_block_playground,
        R.string.main_inactive_large_block_playground_description,
        WebUrlConstant.PLAYGROUND_BASE_URL,
        R.drawable.ic_playground_orange,
        "playground_community"
    ),
}
