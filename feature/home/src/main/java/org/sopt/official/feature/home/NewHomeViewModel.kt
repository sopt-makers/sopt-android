package org.sopt.official.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.home.model.RecentCalendar
import org.sopt.official.domain.home.model.ScheduleType
import org.sopt.official.domain.home.model.ScheduleType.EVENT
import org.sopt.official.domain.home.model.UserInfo
import org.sopt.official.domain.home.model.UserStatus
import org.sopt.official.domain.home.model.UserStatus.ACTIVE
import org.sopt.official.domain.home.model.UserStatus.INACTIVE
import org.sopt.official.domain.home.model.UserStatus.UNAUTHENTICATED
import org.sopt.official.domain.home.repository.HomeRepository
import org.sopt.official.domain.home.result.successOr
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.HomeUiState
import org.sopt.official.feature.home.model.HomeUiState.ActiveMember
import org.sopt.official.feature.home.model.HomeUiState.InactiveMember
import org.sopt.official.feature.home.model.HomeUiState.Unauthenticated
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel
import org.sopt.official.feature.home.model.Schedule
import javax.inject.Inject

@HiltViewModel
internal class NewHomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val viewModelState: MutableStateFlow<HomeViewModelState> = MutableStateFlow(HomeViewModelState())

    val uiState: StateFlow<HomeUiState> = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState(),
        )

    fun refreshAll() {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val userInfoDeferred = async { homeRepository.getUserInfo() }
            val userDescriptionDeferred = async { homeRepository.getHomeDescription() }
            val recentCalendarDeferred = async { homeRepository.getRecentCalendar() }

            val userInfo = userInfoDeferred.await().successOr(UserInfo(UserInfo.User()))
            val userDescription =
                userDescriptionDeferred.await().successOr(UserInfo.UserDescription())
            val recentCalendar = recentCalendarDeferred.await().successOr(RecentCalendar())

            viewModelState.update {
                it.copy(
                    isError = false, // 반복 에러 대응 필요
                    isLoading = false,
                    userState = userInfo.user.userStatus,
                    hasNotification = userInfo.isAllConfirm.not(),
                    generations = userInfo.user.generationList.toImmutableList(),
                    activityDescription = userDescription.activityDescription,
                    scheduleDate = recentCalendar.date,
                    scheduleType = recentCalendar.scheduleType,
                    scheduleTitle = recentCalendar.title,
                )
            }
        }
    }
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val userState: UserStatus = UNAUTHENTICATED,
    val hasNotification: Boolean = false,
    val activityDescription: String = "",
    val generations: ImmutableList<Long> = persistentListOf(),
    val scheduleType: ScheduleType = EVENT,
    val scheduleDate: String = "",
    val scheduleTitle: String = "",
) {

    fun toUiState(): HomeUiState = when (userState) {
        UNAUTHENTICATED -> Unauthenticated(
            isLoading = isLoading,
            isError = isError,
        )

        ACTIVE -> ActiveMember(
            isLoading = isLoading,
            isError = isError,
            hasNotification = false,
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(
                activityDescription = activityDescription,
                generations = generations,
                isActivated = true,
            ), homeSoptScheduleModel = HomeSoptScheduleModel(
                type = Schedule.from(scheduleType),
                date = scheduleDate,
                title = scheduleTitle,
            )
        )

        INACTIVE -> InactiveMember(
            isLoading = isLoading,
            isError = isError,
            hasNotification = false,
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(
                activityDescription = activityDescription,
                generations = generations,
                isActivated = false,
            ), homeSoptScheduleModel = HomeSoptScheduleModel(
                type = Schedule.from(scheduleType),
                date = scheduleDate,
                title = scheduleTitle,
            )
        )
    }
}
