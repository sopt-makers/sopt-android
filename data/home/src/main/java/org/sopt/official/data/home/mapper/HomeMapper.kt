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
package org.sopt.official.data.home.mapper

import org.sopt.official.data.home.remote.response.HomeAppServiceResponseDto
import org.sopt.official.data.home.remote.response.HomeDescriptionResponseDto
import org.sopt.official.data.home.remote.response.HomeFloatingToastDto
import org.sopt.official.data.home.remote.response.HomePopularPostResponseDto
import org.sopt.official.data.home.remote.response.HomeReviewFormResponseDto
import org.sopt.official.data.home.remote.response.RecentCalendarResponseDto
import org.sopt.official.data.home.remote.response.UserMainResponseDto
import org.sopt.official.domain.home.model.AppService
import org.sopt.official.domain.home.model.FloatingToast
import org.sopt.official.domain.home.model.PopularPost
import org.sopt.official.domain.home.model.RecentCalendar
import org.sopt.official.domain.home.model.ReviewForm
import org.sopt.official.domain.home.model.UserInfo
import org.sopt.official.domain.home.model.UserInfo.User
import org.sopt.official.domain.home.model.UserInfo.UserDescription

internal fun UserMainResponseDto.toDomain(): UserInfo = UserInfo(
    user = user.toDomain(),
    isAllConfirm = isAllConfirm,
)

internal fun UserMainResponseDto.UserResponseDto.toDomain(): User = User(
    status = status,
    name = name.orEmpty(),
    profileImage = profileImage.orEmpty(),
    generationList = generationList ?: emptyList()
)

internal fun RecentCalendarResponseDto.toDomain(): RecentCalendar = RecentCalendar(
    date = date,
    type = type,
    title = title,
)

internal fun HomeDescriptionResponseDto.toDomain(): UserDescription = UserDescription(
    activityDescription = activityDescription,
)

internal fun HomeAppServiceResponseDto.toDomain(): AppService = AppService(
    serviceName = serviceName,
    displayAlarmBadge = displayAlarmBadge,
    alarmBadge = alarmBadge,
    iconUrl = iconUrl,
    deepLink = deepLink,
)

internal fun HomeReviewFormResponseDto.toDomain(): ReviewForm = ReviewForm(
    title = this.title,
    subTitle = this.subTitle,
    actionButtonName = this.actionButtonName,
    linkUrl = this.linkUrl,
    isActive = this.isActive
)

internal fun HomeFloatingToastDto.toDomain(): FloatingToast = FloatingToast(
    imageUrl = this.imageUrl,
    title = this.title,
    expandedSubTitle = this.expandedSubTitle,
    collapsedSubtitle = this.collapsedSubtitle,
    actionButtonName = this.actionButtonName,
    linkUrl = this.linkUrl,
    active = this.isActive
)

internal fun HomePopularPostResponseDto.toDomain(): PopularPost = PopularPost(
    profileImage = this.profileImage,
    name = this.name,
    generationAndPart = this.generationAndPart,
    rank = this.rank,
    category = this.category,
    title = this.title,
    content = this.content,
    webLink = this.webLink,
    id = this.id
)
