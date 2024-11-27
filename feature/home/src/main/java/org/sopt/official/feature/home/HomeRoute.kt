package org.sopt.official.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.feature.home.component.HomeShortcutButtons
import org.sopt.official.feature.home.component.HomeSoptScheduleDashboard
import org.sopt.official.feature.home.component.HomeTopBar
import org.sopt.official.feature.home.component.HomeUserSoptLogDashBoard
import org.sopt.official.feature.home.component.HomeUserSoptLogModel
import org.sopt.official.feature.home.component.UserSoptState
import org.sopt.official.feature.home.component.UserSoptState.Member
import org.sopt.official.feature.home.component.UserSoptState.NonMember

@Composable
internal fun HomeRoute() {
    HomeScreen()
}

@Composable
private fun HomeScreen(
    userSoptState: UserSoptState = Member(
        isActivated = false,
        generations = listOf(1, 2, 3, 4, 5, 6, 7)
    ),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        HomeTopBar(
            hasNotification = true,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        HomeUserSoptLogDashBoard(
            homeUserSoptLogModel = HomeUserSoptLogModel(
                activityDescription = "<b>김승환</b>님은\nSOPT와 15개월째"
            ),
            userSoptState = userSoptState,
        )
        when (userSoptState) {
            is Member -> {
                Spacer(modifier = Modifier.height(height = 12.dp))
                HomeSoptScheduleDashboard()
                Spacer(modifier = Modifier.height(height = 12.dp))
            }

            is NonMember -> {
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
        HomeScreen()
    }
}
