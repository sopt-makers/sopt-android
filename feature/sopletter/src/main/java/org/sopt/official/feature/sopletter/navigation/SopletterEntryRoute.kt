package org.sopt.official.feature.sopletter.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.main.SopletterMainRoute
import org.sopt.official.feature.sopletter.onboarding.SopletterOnboardingRoute

@Composable
fun SopletterEntryRoute(
    navigateToNickname: (String, Int) -> Unit,
    navigateToHome: () -> Unit,
    navigateUp: () -> Unit,
    navigateToTopic: () -> Unit,
    navigateToWrite: (Long?) -> Unit,
    navigateToPrint: (Long?) -> Unit,
    viewModel: SopletterEntryViewModel = hiltViewModel(),
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsStateWithLifecycle()

    when (isOnboardingCompleted) {
        true -> {
            SopletterMainRoute(
                navigateUp = navigateUp,
                navigateToTopic = navigateToTopic,
                navigateToWrite = navigateToWrite,
                navigateToPrint = navigateToPrint,
            )
        }

        false -> {
            SopletterOnboardingRoute(
                navigateToNickname = navigateToNickname,
                navigateToHome = navigateToHome,
            )
        }

        null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SoptTheme.colors.background),
            )
        }
    }
}
