package org.sopt.official.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.auth.model.UserStatus.UNAUTHENTICATED
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.home.component.HomeErrorDialog
import org.sopt.official.feature.home.component.HomeProgressIndicator
import org.sopt.official.feature.home.component.HomeShortcutButtonsForMember
import org.sopt.official.feature.home.component.HomeShortcutButtonsForVisitor
import org.sopt.official.feature.home.component.HomeSoptScheduleDashboard
import org.sopt.official.feature.home.component.HomeTopBarForMember
import org.sopt.official.feature.home.component.HomeTopBarForVisitor
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForMember
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForVisitor
import org.sopt.official.feature.home.model.HomeNavigation
import org.sopt.official.feature.home.model.HomeNavigation.HomeDashboardNavigation
import org.sopt.official.feature.home.model.HomeNavigation.HomeShortcutNavigation
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.HomeUiState.Member
import org.sopt.official.feature.home.model.HomeUiState.Unauthenticated
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel

@Composable
internal fun HomeRoute(
    userStatus: UserStatus,
    homeNavigation: HomeNavigation,
    newHomeViewModel: NewHomeViewModel = hiltViewModel(),
) {
    val uiState by newHomeViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userStatus) {
        if (userStatus != UNAUTHENTICATED) newHomeViewModel.refreshAll()
    }

    when (val state = uiState) {
        is Unauthenticated -> HomeScreenForVisitor(homeShortcutEvent = homeNavigation as HomeShortcutNavigation)
        is Member -> {
            HomeScreenForMember(
                homeDashboardEvent = homeNavigation as HomeDashboardNavigation,
                homeShortcutEvent = homeNavigation as HomeShortcutNavigation,
                hasNotification = state.hasNotification,
                homeUserSoptLogDashboardModel = state.homeUserSoptLogDashboardModel,
                homeSoptScheduleModel = state.homeSoptScheduleModel,
            )
        }
    }

    if (uiState.isLoading) HomeProgressIndicator()
    if (uiState.isError) HomeErrorDialog(onCheckClick = { newHomeViewModel.refreshAll() })
}

@Composable
private fun HomeScreenForMember(
    homeDashboardEvent: HomeDashboardNavigation,
    homeShortcutEvent: HomeShortcutNavigation,
    hasNotification: Boolean,
    homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel,
    homeSoptScheduleModel: HomeSoptScheduleModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        HomeTopBarForMember(
            hasNotification = hasNotification,
            onNotificationClick = homeDashboardEvent::navigateToNotification,
            onSettingClick = homeDashboardEvent::navigateToSetting,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeUserSoptLogDashboardForMember(
            onSoptlogClick = homeDashboardEvent::navigateToSoptlog,
            homeUserSoptLogDashboardModel = homeUserSoptLogDashboardModel,
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        HomeSoptScheduleDashboard(
            homeSoptScheduleModel = homeSoptScheduleModel,
            isActivatedGeneration = homeUserSoptLogDashboardModel.isActivated,
            onScheduleClick = homeDashboardEvent::navigateToSchedule,
            onAttendanceButtonClick = homeDashboardEvent::navigateToAttendance,
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        HomeShortcutButtonsForMember(
            onPlaygroundClick = homeShortcutEvent::navigateToPlayground,
            onStudyClick = homeShortcutEvent::navigateToPlaygroundGroup,
            onMemberClick = homeShortcutEvent::navigateToPlaygroundMember,
            onProjectClick = homeShortcutEvent::navigateToPlaygroundProject,
        )
    }
}

@Composable
private fun HomeScreenForVisitor(
    homeShortcutEvent: HomeShortcutNavigation,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        HomeTopBarForVisitor(onSettingClick = { })
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeUserSoptLogDashboardForVisitor()
        Spacer(modifier = Modifier.height(height = 36.dp))
        Text(
            text = "SOPT를 더 알고 싶다면, 둘러보세요",
            style = typography.heading20B,
            color = colors.onBackground,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeShortcutButtonsForVisitor(
            onHomePageClick = homeShortcutEvent::navigateToSoptHomepage,
            onPlaygroundClick = homeShortcutEvent::navigateToSoptReview,
            onProjectClick = homeShortcutEvent::navigateToSoptProject,
            onInstagramClick = homeShortcutEvent::navigateToSoptInstagram,
        )
    }
}
