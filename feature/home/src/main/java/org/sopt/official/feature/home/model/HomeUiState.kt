package org.sopt.official.feature.home.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.sopt.official.feature.home.model.Schedule.EVENT

@Stable
internal sealed interface HomeUiState {
    val isLoading: Boolean
    val isError: Boolean

    @Stable
    sealed interface Member : HomeUiState {
        val hasNotification: Boolean
        val homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel
        val homeSoptScheduleModel: HomeSoptScheduleModel
    }

    @Immutable
    data class Unauthenticated(
        override val isLoading: Boolean,
        override val isError: Boolean,
    ) : HomeUiState

    @Immutable
    data class ActiveMember(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val hasNotification: Boolean = false,
        override val homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(),
        override val homeSoptScheduleModel: HomeSoptScheduleModel = HomeSoptScheduleModel(),
    ) : Member

    @Immutable
    data class InactiveMember(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val hasNotification: Boolean = false,
        override val homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(),
        override val homeSoptScheduleModel: HomeSoptScheduleModel = HomeSoptScheduleModel(),
    ) : Member
}

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
    val activityDescription: String = "",
    val generations: ImmutableList<Long> = persistentListOf(),
    val isActivated: Boolean = false,
) {
    private val regex by lazy { Regex("<b>(.*?)</b>") }
    val emphasizedDescription: String =
        regex.find(activityDescription)?.groups?.get(1)?.value.orEmpty()
    val remainingDescription: String =
        " " + regex.replace(activityDescription, "").trim().replace("<br>", "\n")
    val recentGeneration: String =
        "${generations.maxOrNull() ?: 0}기 " + if (isActivated) "활동 중" else "수료"
    val lastGenerations: ImmutableList<Long> =
        generations.filter { it != generations.max() }.sortedDescending().toPersistentList()
}
