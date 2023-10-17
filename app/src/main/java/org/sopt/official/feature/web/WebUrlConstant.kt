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
package org.sopt.official.feature.web

object WebUrlConstant {
    val SOPT_OFFICIAL_PAGE_URL = toSoptUrl("")
    val SOPT_REVIEW_URL = toSoptUrl("review")
    val SOPT_PROJECT_URL = toSoptUrl("project")
    val SOPT_FAQ_URL = toSoptUrl("FAQ")

    val PLAYGROUNG_MEMBER_URL = toPlaygroundUrl("members")
    val PLAYGROUNG_PROJECT_URL = toPlaygroundUrl("projects")
    val PLAYGROUNG_CREW_URL = toPlaygroundUrl("group")

    const val SOPT_INSTAGRAM = "https://www.instagram.com/sopt_official/"
    const val SOPT_OFFICIAL_YOUTUBE = "https://www.youtube.com/@SOPTMEDIA"
    const val NOTICE_PRIVATE_INFO =
        "https://florentine-legend-ffc.notion.site/SOPT-0b378275554d4d65a442310e83c7c988"
    const val NOTICE_SERVICE_RULE =
        "https://florentine-legend-ffc.notion.site/SOPT-69e33dccb59543dd91f5f44ed1250881"
    const val OPINION_GOOGLE_FORM = "https://forms.gle/mB59JD4F1FpKiXBAA"

    private const val SOPT_BASE_URL = "https://sopt.org"
    private const val PLAYGROUNG_BASE_URL = "https://playground.sopt.org"

    private fun toSoptUrl(url: String): String =
        if (url.isNotEmpty()) {
            "$SOPT_BASE_URL/$url"
        } else {
            SOPT_BASE_URL
        }

    private fun toPlaygroundUrl(url: String): String =
        if (url.isNotEmpty()) {
            "$PLAYGROUNG_BASE_URL/$url"
        } else {
            PLAYGROUNG_BASE_URL
        }
}
