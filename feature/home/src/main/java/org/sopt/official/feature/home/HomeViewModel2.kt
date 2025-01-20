package org.sopt.official.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.home.model.UserStatus.UNAUTHENTICATED
import org.sopt.official.domain.home.repository.HomeRepository
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.HomeUiState
import org.sopt.official.feature.home.model.HomeUiState.Loading
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel2 @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _homeUiState: MutableStateFlow<HomeUiState> = MutableStateFlow(Loading)
    val homeUiState: StateFlow<HomeUiState> get() = _homeUiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                val userInfoDeferred = async { homeRepository.getUserInfo() }
                val userDescriptionDeferred = async { homeRepository.getHomeDescription() }
                val recentCalendarDeferred = async { homeRepository.getRecentCalendar() }

                val userInfo = userInfoDeferred.await()
                val userDescription = userDescriptionDeferred.await()
                val recentCalendar = recentCalendarDeferred.await()
                val isLogin = userInfo.user.userStatus != UNAUTHENTICATED

                HomeUiState.Success(
                    isLogin = isLogin,
                    hasNotification = userInfo.isAllConfirm.not(),
                    homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel(
                        isLogin = isLogin,
                        activityDescription = userDescription.activityDescription,
                        generations = userInfo.user.generationList.toImmutableList(),
                        _isActivated = userInfo.user.isActivated,
                    ),
                    homeSoptScheduleModel = HomeSoptScheduleModel(
                        date = recentCalendar.date,
                        type = recentCalendar.scheduleType,
                        title = recentCalendar.title,
                    )
                )
            }.onSuccess { homeUiState ->
                _homeUiState.value = homeUiState
            }.onFailure {
                _homeUiState.value = HomeUiState.Error(it)
            }
        }
    }
}
