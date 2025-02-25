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
import kotlinx.collections.immutable.ImmutableList
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.auth.model.UserStatus.UNAUTHENTICATED
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.home.component.HomeEnjoySoptServicesBlock
import org.sopt.official.feature.home.component.HomeErrorDialog
import org.sopt.official.feature.home.component.HomeProgressIndicator
import org.sopt.official.feature.home.component.HomeShortcutButtonsForMember
import org.sopt.official.feature.home.component.HomeShortcutButtonsForVisitor
import org.sopt.official.feature.home.component.HomeSoptScheduleDashboard
import org.sopt.official.feature.home.component.HomeTopBarForMember
import org.sopt.official.feature.home.component.HomeTopBarForVisitor
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForMember
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForVisitor
import org.sopt.official.feature.home.model.HomeAppService
import org.sopt.official.feature.home.model.HomeNavigation
import org.sopt.official.feature.home.model.HomeNavigation.HomeAppServicesNavigation
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
        is Unauthenticated -> {
            HomeScreenForVisitor(
                homeShortcutNavigation = homeNavigation as HomeShortcutNavigation,
                homeAppServicesNavigation = homeNavigation as HomeAppServicesNavigation,
                homeDashboardNavigation = homeNavigation as HomeDashboardNavigation,
                homeAppServices = uiState.homeServices,
            )
        }

        is Member -> {
            HomeScreenForMember(
                homeDashboardNavigation = homeNavigation as HomeDashboardNavigation,
                homeShortcutNavigation = homeNavigation as HomeShortcutNavigation,
                homeAppServicesNavigation = homeNavigation as HomeAppServicesNavigation,
                hasNotification = state.hasNotification,
                homeUserSoptLogDashboardModel = state.homeUserSoptLogDashboardModel,
                homeSoptScheduleModel = state.homeSoptScheduleModel,
                homeAppServices = uiState.homeServices,
            )
        }
    }

    if (uiState.isLoading) HomeProgressIndicator()
    if (uiState.isError) HomeErrorDialog(onCheckClick = { newHomeViewModel.refreshAll() })
}

@Composable
private fun HomeScreenForMember(
    homeDashboardNavigation: HomeDashboardNavigation,
    homeShortcutNavigation: HomeShortcutNavigation,
    homeAppServicesNavigation: HomeAppServicesNavigation,
    hasNotification: Boolean,
    homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel,
    homeSoptScheduleModel: HomeSoptScheduleModel,
    homeAppServices: ImmutableList<HomeAppService>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        HomeTopBarForMember(
            hasNotification = hasNotification,
            onNotificationClick = homeDashboardNavigation::navigateToNotification,
            onSettingClick = homeDashboardNavigation::navigateToSetting,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeUserSoptLogDashboardForMember(
            onDashboardClick = homeDashboardNavigation::navigateToSoptlog,
            homeUserSoptLogDashboardModel = homeUserSoptLogDashboardModel,
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        HomeSoptScheduleDashboard(
            homeSoptScheduleModel = homeSoptScheduleModel,
            isActivatedGeneration = homeUserSoptLogDashboardModel.isActivated,
            onScheduleClick = homeDashboardNavigation::navigateToSchedule,
            onAttendanceButtonClick = homeDashboardNavigation::navigateToAttendance,
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        HomeShortcutButtonsForMember(
            onPlaygroundClick = homeShortcutNavigation::navigateToPlayground,
            onStudyClick = homeShortcutNavigation::navigateToPlaygroundGroup,
            onMemberClick = homeShortcutNavigation::navigateToPlaygroundMember,
            onProjectClick = homeShortcutNavigation::navigateToPlaygroundProject,
        )
        Spacer(modifier = Modifier.height(height = 40.dp))
        HomeEnjoySoptServicesBlock(
            appServices = homeAppServices,
            onAppServiceClick = homeAppServicesNavigation::navigateToDeepLink,
        )
    }
}

@Composable
private fun HomeScreenForVisitor(
    homeShortcutNavigation: HomeShortcutNavigation,
    homeDashboardNavigation: HomeDashboardNavigation,
    homeAppServicesNavigation: HomeAppServicesNavigation,
    homeAppServices: ImmutableList<HomeAppService>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        HomeTopBarForVisitor(onSettingClick = homeDashboardNavigation::navigateToSetting)
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeUserSoptLogDashboardForVisitor(onDashboardClick = homeDashboardNavigation::navigateToSoptlog)
        Spacer(modifier = Modifier.height(height = 36.dp))
        Text(
            text = "SOPT를 더 알고 싶다면, 둘러보세요",
            style = typography.heading20B,
            color = colors.onBackground,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeShortcutButtonsForVisitor(
            onHomePageClick = homeShortcutNavigation::navigateToSoptHomepage,
            onPlaygroundClick = homeShortcutNavigation::navigateToSoptReview,
            onProjectClick = homeShortcutNavigation::navigateToSoptProject,
            onInstagramClick = homeShortcutNavigation::navigateToSoptInstagram,
        )
        Spacer(modifier = Modifier.height(height = 40.dp))
        HomeEnjoySoptServicesBlock(
            appServices = homeAppServices,
            onAppServiceClick = homeAppServicesNavigation::navigateToDeepLink,
        )
    }
}
