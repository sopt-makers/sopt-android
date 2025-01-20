package org.sopt.official.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.home.component.HomeShortcutButtons
import org.sopt.official.feature.home.component.HomeSoptScheduleDashboard
import org.sopt.official.feature.home.component.HomeTopBar
import org.sopt.official.feature.home.component.HomeUserSoptLogDashBoard
import org.sopt.official.feature.home.model.HomeSoptScheduleModel
import org.sopt.official.feature.home.model.HomeUiState.Error
import org.sopt.official.feature.home.model.HomeUiState.Loading
import org.sopt.official.feature.home.model.HomeUiState.Success
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel
import org.sopt.official.feature.home.model.dummyHomeUiState

@Composable
internal fun HomeRoute(
    homeViewModel: HomeViewModel2 = hiltViewModel(),
) {
    val uiState = homeViewModel.homeUiState.collectAsStateWithLifecycle()

    when (val state = uiState.value) {
        is Success -> {
            HomeScreen(
                isLogin = state.isLogin,
                hasNotification = state.hasNotification,
                homeUserSoptLogDashboardModel = state.homeUserSoptLogDashboardModel,
                homeSoptScheduleModel = state.homeSoptScheduleModel,
                onNotificationClick = {},
                onSettingClick = {},
                onDashboardClick = {},
                onAttendanceButtonClick = {},
            )
        }

        is Loading -> {
            // 로딩 상태 UI 처리
        }

        is Error -> {
            // 에러 상태 UI 처리
        }
    }
}

@Composable
private fun HomeScreen(
    isLogin: Boolean,
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
        HomeTopBar(
            isLogin = isLogin,
            hasNotification = hasNotification,
            onNotificationClick = onNotificationClick,
            onSettingClick = onSettingClick,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeUserSoptLogDashBoard(homeUserSoptLogDashboardModel = homeUserSoptLogDashboardModel)

        when (isLogin) {
            true -> {
                Spacer(modifier = Modifier.height(height = 12.dp))
                HomeSoptScheduleDashboard(
                    homeSoptScheduleModel = homeSoptScheduleModel,
                    isActivatedGeneration = homeUserSoptLogDashboardModel.isActivated,
                    onDashboardClick = onDashboardClick,
                    onAttendanceButtonClick = onAttendanceButtonClick,
                )
                Spacer(modifier = Modifier.height(height = 12.dp))
            }

            false -> {
                Spacer(modifier = Modifier.height(height = 36.dp))
                Text(
                    text = "SOPT를 더 알고 싶다면, 둘러보세요",
                    style = typography.heading20B,
                    color = colors.onBackground,
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
            }
        }
        HomeShortcutButtons()
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    SoptTheme {
        Surface {
            HomeScreen(
                isLogin = dummyHomeUiState.isLogin,
                hasNotification = dummyHomeUiState.hasNotification,
                homeUserSoptLogDashboardModel = dummyHomeUiState.homeUserSoptLogDashboardModel,
                homeSoptScheduleModel = dummyHomeUiState.homeSoptScheduleModel,
                onNotificationClick = {},
                onSettingClick = {},
                onDashboardClick = {},
                onAttendanceButtonClick = {},
            )
        }
    }
}
