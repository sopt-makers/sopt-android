/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.home.mapper

import org.sopt.official.domain.home.model.FloatingToast
import org.sopt.official.domain.home.model.LatestPost
import org.sopt.official.domain.home.model.PopularPost
import org.sopt.official.domain.home.model.ReviewForm
import org.sopt.official.feature.home.model.HomeFloatingToastData
import org.sopt.official.feature.home.model.HomePlaygroundPostModel
import org.sopt.official.feature.home.model.HomeSurveyData

fun ReviewForm.toModel(): HomeSurveyData = HomeSurveyData(
    title = title,
    description = subTitle,
    buttonText = actionButtonName,
    surveyLink = linkUrl,
    isActive = isActive
)

fun FloatingToast.toModel(): HomeFloatingToastData = HomeFloatingToastData(
    imageUrl = imageUrl,
    title = title,
    toastDescription = expandedSubTitle,
    buttonDescription = collapsedSubtitle,
    buttonText = actionButtonName,
    linkUrl = linkUrl,
    active = active
)

fun PopularPost.toModel(): HomePlaygroundPostModel = HomePlaygroundPostModel(
    userId = this.userId,
    postId = this.id,
    title = this.title,
    content = this.content,
    category = this.category,
    label = "실시간 인기 ${this.rank}위",
    profileImage = this.profileImage,
    name = this.name,
    generationAndPart = this.generationAndPart,
    webLink = this.webLink
)

fun LatestPost.toModel(): HomePlaygroundPostModel = HomePlaygroundPostModel(
    userId = this.userId,
    postId = this.id,
    title = this.title,
    content = this.content,
    category = this.category,
    label = "NEW",
    profileImage = this.profileImage,
    name = this.name,
    generationAndPart = this.generationAndPart,
    webLink = this.webLink,
    isOutdated = this.isOutdated
)
