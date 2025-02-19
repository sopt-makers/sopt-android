package org.sopt.official.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.home.component.HomeErrorDialog
import org.sopt.official.feature.home.component.HomeShortcutButtonsForMember
import org.sopt.official.feature.home.component.HomeShortcutButtonsForVisitor
import org.sopt.official.feature.home.component.HomeSoptScheduleDashboard
import org.sopt.official.feature.home.component.HomeTopBarForMember
import org.sopt.official.feature.home.component.HomeTopBarForVisitor
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForMember
import org.sopt.official.feature.home.component.HomeUserSoptLogDashboardForVisitor
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.HomeUiState.Member
import org.sopt.official.feature.home.model.HomeUiState.Unauthenticated
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel

@Composable
internal fun HomeRoute(
    newHomeViewModel: NewHomeViewModel = hiltViewModel(),
) {
    val uiState by newHomeViewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is Unauthenticated -> HomeScreenForVisitor()
            is Member -> {
                HomeScreenForMember(
                    hasNotification = state.hasNotification,
                    homeUserSoptLogDashboardModel = state.homeUserSoptLogDashboardModel,
                    homeSoptScheduleModel = state.homeSoptScheduleModel,
                    onNotificationClick = {},
                    onSettingClick = {},
                    onDashboardClick = {},
                    onAttendanceButtonClick = {},
                )
            }
        }

        if (uiState.isLoading) {
            Box(
                contentAlignment = Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colors.background.copy(alpha = 0.55f)),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(width = 32.dp),
                    color = colorScheme.secondary,
                    trackColor = colorScheme.surfaceVariant,
                    strokeWidth = 4.dp,
                )
            }
        }

        if (uiState.isError) HomeErrorDialog(onCheckClick = { newHomeViewModel.refreshAll() })
    }
}

@Composable
private fun HomeScreenForMember(
    hasNotification: Boolean,
    homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel,
    homeSoptScheduleModel: HomeSoptScheduleModel,
    onNotificationClick: () -> Unit,
    onSettingClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onAttendanceButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        HomeTopBarForMember(
            hasNotification = hasNotification,
            onNotificationClick = onNotificationClick,
            onSettingClick = onSettingClick,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeUserSoptLogDashboardForMember(homeUserSoptLogDashboardModel = homeUserSoptLogDashboardModel)
        Spacer(modifier = Modifier.height(height = 12.dp))
        HomeSoptScheduleDashboard(
            homeSoptScheduleModel = homeSoptScheduleModel,
            isActivatedGeneration = homeUserSoptLogDashboardModel.isActivated,
            onDashboardClick = onDashboardClick,
            onAttendanceButtonClick = onAttendanceButtonClick,
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        HomeShortcutButtonsForMember()
    }
}

@Composable
private fun HomeScreenForVisitor() {
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
        HomeShortcutButtonsForVisitor()
    }
}
