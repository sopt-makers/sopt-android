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
package org.sopt.official.feature.home.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.sopt.official.feature.home.R.drawable.img_poke
import org.sopt.official.feature.home.R.drawable.img_soptamp
import org.sopt.official.feature.home.R.drawable.img_soptmadi
import org.sopt.official.feature.home.model.Schedule.EVENT

@Stable
sealed interface HomeUiState {
    val isLoading: Boolean
    val isError: Boolean
    val homeServices: ImmutableList<HomeAppService>
    val surveyData: HomeSurveyData
    val floatingToastData: HomeFloatingToastData

    @Stable
    sealed interface Member : HomeUiState {
        val hasNotification: Boolean
        val homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel
        val homeSoptScheduleModel: HomeSoptScheduleModel
        val popularPosts: ImmutableList<HomePlaygroundPostModel>
        val latestPosts: ImmutableList<HomePlaygroundPostModel>
    }

    @Immutable
    data class Unauthenticated(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val homeServices: ImmutableList<HomeAppService> = persistentListOf(),
        override val surveyData: HomeSurveyData = HomeSurveyData(),
        override val floatingToastData: HomeFloatingToastData = HomeFloatingToastData(),
    ) : HomeUiState

    @Immutable
    data class ActiveMember(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val homeServices: ImmutableList<HomeAppService> = persistentListOf(),
        override val hasNotification: Boolean = false,
        override val homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(),
        override val homeSoptScheduleModel: HomeSoptScheduleModel = HomeSoptScheduleModel(),
        override val surveyData: HomeSurveyData = HomeSurveyData(),
        override val floatingToastData: HomeFloatingToastData = HomeFloatingToastData(),
        override val popularPosts: ImmutableList<HomePlaygroundPostModel> = persistentListOf(),
        override val latestPosts: ImmutableList<HomePlaygroundPostModel> = persistentListOf(),
    ) : Member

    @Immutable
    data class InactiveMember(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val homeServices: ImmutableList<HomeAppService> = persistentListOf(),
        override val hasNotification: Boolean = false,
        override val homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(),
        override val homeSoptScheduleModel: HomeSoptScheduleModel = HomeSoptScheduleModel(),
        override val surveyData: HomeSurveyData = HomeSurveyData(),
        override val floatingToastData: HomeFloatingToastData = HomeFloatingToastData(),
        override val popularPosts: ImmutableList<HomePlaygroundPostModel> = persistentListOf(),
        override val latestPosts: ImmutableList<HomePlaygroundPostModel> = persistentListOf(),
    ) : Member
}

@Immutable
data class HomeSurveyData(
    val title: String = "솝커톤 어땠나요?",
    val description: String = "여러분의 솝커톤 이야기를 들려주세요!",
    val buttonText: String = "지금 솝커톤 후기 쓰러가기",
    val surveyLink: String = "",
    val isActive: Boolean = false
)

@Immutable
data class HomeFloatingToastData(
    val imageUrl: String = "",
    val title: String = "점수 2배! 깜짝 미션 오픈",
    val toastDescription: String = "지금 바로 미션에 도전해보세요",
    val buttonDescription: String = "솝탬프",
    val buttonText: String = "미션 보기",
    val linkUrl: String = "",
    val active: Boolean = false
)

@Immutable
data class HomePlaygroundPostModel(
    val userId: Int? = null,
    val postId: Int = -1,
    val title: String = "",
    val content: String = "",
    val category: String = "",
    val label: String = "",
    val profileImage: String = "",
    val name: String = "",
    val generationAndPart: String = "",
    val webLink: String = "",
    val isOutdated: Boolean = false
)

@Immutable
data class HomeSoptScheduleModel(
    val type: Schedule = EVENT,
    val date: String = "",
    val title: String = "",
) {
    val formattedDate = date.replace('-', '.')
}

@Immutable
data class HomeUserSoptLogDashboardModel(
    val userProfile: String = "",
    val activityDescription: String = "",
    val generations: ImmutableList<Long> = persistentListOf(),
    val isActivated: Boolean = false,
) {
    private val regex by lazy { Regex("<b>(.*?)</b>") }
    val recentGeneration: Int = generations.maxOrNull()?.toInt() ?: 0
    val emphasizedDescription: String =
        regex.find(activityDescription)?.groups?.get(1)?.value.orEmpty()
    val remainingDescription: String =
        " " + regex.replace(activityDescription, "").trim().replace("<br>", "\n")
    val recentGenerationDescription: String =
        "${recentGeneration}기 " + if (isActivated) "활동 중" else "수료"
    val lastGenerations: ImmutableList<Long> =
        generations.filter { it != generations.max() }.sortedDescending().toPersistentList()
}

@Immutable
data class HomeAppService(
    val serviceName: String = "",
    val isShowAlarmBadge: Boolean = false,
    val alarmBadgeContent: String = "",
    val iconUrl: String = "",
    val deepLink: String = "",
    @DrawableRes val defaultIcon: Int? = null,
)

val defaultAppServices: ImmutableList<HomeAppService> = persistentListOf(
    HomeAppService(
        serviceName = "콕찌르기",
        defaultIcon = img_poke,
    ),
    HomeAppService(
        serviceName = "솝탬프",
        defaultIcon = img_soptamp,
    ),
)
