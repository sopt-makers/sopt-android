/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.main.model

import org.sopt.official.common.BuildConfig

object SoptWebLink {
    const val OFFICIAL_HOMEPAGE = "https://sopt.org"
    const val PROJECT = "https://sopt.org/project"
    const val REVIEW = "https://sopt.org/review"
    const val INSTAGRAM = "https://www.instagram.com/sopt_official"
}

object PlaygroundWebLink {
    const val OFFICIAL_HOMEPAGE = BuildConfig.PLAYGROUND_API
    const val MEMBER = OFFICIAL_HOMEPAGE + "members"
    const val EDIT_PROFILE = OFFICIAL_HOMEPAGE + "members/edit"
    const val PROJECT = OFFICIAL_HOMEPAGE + "projects"
    const val GROUP_STUDY = OFFICIAL_HOMEPAGE + "group"

    const val WRITE = OFFICIAL_HOMEPAGE + "feed/upload"
    const val MAKE_FLASH = OFFICIAL_HOMEPAGE + "group/make/flash"
    const val MAKE_MOIM = OFFICIAL_HOMEPAGE + "group/make"
    const val WRITE_FEED = OFFICIAL_HOMEPAGE + "group?modal=create-feed"
    const val UPLOAD_REVIEW = OFFICIAL_HOMEPAGE + "blog"
}
