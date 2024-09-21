package org.sopt.official.feature.fortune

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.fortune.feature.fortundDetail.FortuneDetail
import org.sopt.official.feature.fortune.feature.fortundDetail.FortuneDetailRoute
import org.sopt.official.feature.fortune.feature.home.Home
import org.sopt.official.feature.fortune.feature.home.HomeRoute

@Composable
fun MainScreen(
    navController: NavController = rememberNavController(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            // TODO: Navigate to NotificationActivity
                        },
                    tint = SoptTheme.colors.onBackground
                )
            }
        },
        content = { paddingValue ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SoptTheme.colors.background)
                    .padding(paddingValue)
            ) {
                NavHost(
                    navController = rememberNavController(),
                    startDestination = Home
                ) {
                    composable<Home> {
                        HomeRoute(
                            paddingValue = paddingValue,
                            navigateToFortuneDetail = { date ->
                                navController.navigate(FortuneDetail(date))
                            }
                        )
                    }

                    composable<FortuneDetail> { backStackEntry ->
                        val items = backStackEntry.toRoute<FortuneDetail>()
                        FortuneDetailRoute(
                            date = items.date
                        )
                    }
                }
            }
        }

    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SoptTheme {
        MainScreen()
    }
}
