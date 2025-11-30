package org.sopt.official.feature.poke.bridge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.sopt.official.feature.poke.main.PokeScreen
import org.sopt.official.feature.poke.navigation.navigateToPokeNotification
import org.sopt.official.feature.poke.onboarding.OnboardingScreen
import org.sopt.official.model.UserStatus

@Composable
fun PokeEntryRoute(
    paddingValues: PaddingValues,
    navController: NavController,
    userStatus: UserStatus,
    viewModel: PokeEntryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.isError) {
        if (uiState.isError) {
            navController.popBackStack()
        }
    }
    when {
        uiState.isLoading || uiState.isNewPoke == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(width = 32.dp),
                    color = colorScheme.secondary,
                    trackColor = colorScheme.surfaceVariant,
                    strokeWidth = 4.dp,
                )
            }
        }

        // 신규 유저 -> 온보딩 화면
        uiState.isNewPoke == true -> {
            OnboardingScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
                navigateUp = navController::popBackStack,
                navigateToPokeMain = viewModel::updateToOldUser,
            )
        }

        // 기존 유저(혹은 온보딩 완료 후) -> 콕 찌르기 메인 화면
        else -> {
            PokeScreen(
                paddingValues = paddingValues,
                userStatus = userStatus,
                navigateToPokeNotification = {
                    navController.navigateToPokeNotification(userStatus.name)
                },
            )
        }
    }
}
