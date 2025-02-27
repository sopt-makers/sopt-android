package org.sopt.official.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.home.model.AppService
import org.sopt.official.domain.home.model.RecentCalendar
import org.sopt.official.domain.home.model.UserInfo
import org.sopt.official.domain.home.model.UserStatus
import org.sopt.official.domain.home.model.UserStatus.ACTIVE
import org.sopt.official.domain.home.model.UserStatus.INACTIVE
import org.sopt.official.domain.home.model.UserStatus.UNAUTHENTICATED
import org.sopt.official.domain.home.repository.HomeRepository
import org.sopt.official.domain.home.result.successOr
import org.sopt.official.domain.poke.entity.onSuccess
import org.sopt.official.domain.poke.usecase.CheckNewInPokeUseCase
import org.sopt.official.feature.home.model.HomeAppService
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.HomeUiState
import org.sopt.official.feature.home.model.HomeUiState.ActiveMember
import org.sopt.official.feature.home.model.HomeUiState.InactiveMember
import org.sopt.official.feature.home.model.HomeUiState.Unauthenticated
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel
import org.sopt.official.feature.home.model.Schedule
import org.sopt.official.feature.home.model.defaultAppServices
import javax.inject.Inject

@HiltViewModel
internal class NewHomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val checkNewInPokeUseCase: CheckNewInPokeUseCase,
) : ViewModel() {

    private val viewModelState: MutableStateFlow<HomeViewModelState> =
        MutableStateFlow(HomeViewModelState())

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
            val appServiceDeferred = async { homeRepository.getHomeAppService() }

            val userInfo = userInfoDeferred.await().successOr(UserInfo(UserInfo.User()))
            val userDescription =
                userDescriptionDeferred.await().successOr(UserInfo.UserDescription())
            val recentCalendar = recentCalendarDeferred.await().successOr(RecentCalendar())
            val appService = appServiceDeferred.await().successOr(emptyList())

            viewModelState.update {
                it.copy(
                    isError = false, // 반복 에러 대응 필요
                    isLoading = false,
                    userState = userInfo.user.userStatus,
                    userInfo = userInfo,
                    userDescription = userDescription,
                    recentCalendar = recentCalendar,
                    appServices = appService,
                )
            }
        }
    }

    fun fetchIsNewPoke(onComplete: (isNewPoke: Boolean) -> Unit) {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            checkNewInPokeUseCase().onSuccess { result ->
                viewModelState.update {
                    it.copy(isLoading = false)
                }
                onComplete(result.isNew)
            }
        }
    }
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val userState: UserStatus = UNAUTHENTICATED,
    val userInfo: UserInfo = UserInfo(),
    val userDescription: UserInfo.UserDescription = UserInfo.UserDescription(),
    val recentCalendar: RecentCalendar = RecentCalendar(),
    val appServices: List<AppService> = emptyList(),
) {

    fun toUiState(): HomeUiState = when (userState) {
        UNAUTHENTICATED -> Unauthenticated(
            isLoading = isLoading,
            isError = isError,
            homeServices = defaultAppServices,
        )

        ACTIVE -> ActiveMember(
            isLoading = isLoading,
            isError = isError,
            hasNotification = userInfo.isAllConfirm.not(),
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(
                activityDescription = userDescription.activityDescription,
                generations = userInfo.user.generationList.toImmutableList(),
                isActivated = true,
            ),
            homeSoptScheduleModel = HomeSoptScheduleModel(
                type = Schedule.from(recentCalendar.scheduleType),
                date = recentCalendar.date,
                title = recentCalendar.title,
            ),
            homeServices = appServices.map {
                HomeAppService(
                    serviceName = it.serviceName,
                    isShowAlarmBadge = it.displayAlarmBadge,
                    alarmBadgeContent = it.alarmBadge,
                    iconUrl = it.iconUrl,
                    deepLink = it.deepLink,
                )
            }.toImmutableList()
        )

        INACTIVE -> InactiveMember(
            isLoading = isLoading,
            isError = isError,
            hasNotification = userInfo.isAllConfirm.not(),
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(
                activityDescription = userDescription.activityDescription,
                generations = userInfo.user.generationList.toImmutableList(),
                isActivated = false,
            ),
            homeSoptScheduleModel = HomeSoptScheduleModel(
                type = Schedule.from(recentCalendar.scheduleType),
                date = recentCalendar.date,
                title = recentCalendar.title,
            ),
            homeServices = appServices.map {
                HomeAppService(
                    serviceName = it.serviceName,
                    isShowAlarmBadge = it.displayAlarmBadge,
                    alarmBadgeContent = it.alarmBadge,
                    iconUrl = it.iconUrl,
                    deepLink = it.deepLink,
                )
            }.toImmutableList()
        )
    }
}
