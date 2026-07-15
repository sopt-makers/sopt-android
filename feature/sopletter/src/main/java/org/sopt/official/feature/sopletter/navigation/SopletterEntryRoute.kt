package org.sopt.official.feature.sopletter.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.designsystem.SoptTheme

@Composable
fun SopletterEntryRoute(
    navigateToMain: () -> Unit,
    navigateToOnboarding: () -> Unit,
    viewModel: SopletterEntryViewModel = hiltViewModel(),
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsStateWithLifecycle()

    LaunchedEffect(isOnboardingCompleted) {
        when (isOnboardingCompleted) {
            true -> navigateToMain()
            false -> navigateToOnboarding()
            null -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoptTheme.colors.background),
    )
}
