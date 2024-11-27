package org.sopt.official.feature.home.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Stable
sealed interface HomeUiState {
    @Immutable
    data class Success(
        val isLogin: Boolean = false,
        val hasNotification: Boolean = false,
        val homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(isLogin = isLogin),
        val homeSoptScheduleModel: HomeSoptScheduleModel = HomeSoptScheduleModel(),
    ) : HomeUiState

    @Immutable
    data class Error(
        val error: Throwable? = null,
    ) : HomeUiState

    @Immutable
    data object Loading : HomeUiState
}

@Immutable
data class HomeSoptScheduleModel(
    val date: String = "",
    val scheduleType: String = "",
    val scheduleTitle: String = "",
)

@Immutable
data class HomeUserSoptLogDashboardModel(
    val isLogin: Boolean,
    val activityDescription: String = "",
    val generations: ImmutableList<Int> = persistentListOf(),
    private val _isActivated: Boolean = false,
) {
    private val regex by lazy { Regex("<b>(.*?)</b>") }

    val emphasizedDescription: String =
        if (isLogin) regex.find(activityDescription)?.groups?.get(1)?.value.orEmpty() else "안녕하세요.\nSOPT의 열정이 되어주세요!"

    val remainingDescription: String =
        if (isLogin) " " + regex.replace(activityDescription, "").trim() else ""

    val recentGeneration: String = if (isLogin) {
        "${generations.max()}기 " + if (_isActivated) "활동 중" else "수료"
    } else "비회원"

    val isActivated: Boolean get() = if (isLogin) _isActivated else false

    val lastGenerations: ImmutableList<Int> =
        if (isLogin) generations.filter { it != generations.max() }.sortedDescending().toPersistentList() else persistentListOf()
}

val dummyHomeUiState = HomeUiState.Success()
