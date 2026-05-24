package org.sopt.official.sopletter.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.sopt.official.sopletter.onboarding.SopletterOnboardingRoute
import org.sopt.official.sopletter.onboarding.navigation.SopletterOnboarding

@Serializable
data object SopletterGraph

fun NavGraphBuilder.sopletterGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
) {
    navigation<SopletterGraph> (
        startDestination = SopletterOnboarding
    ) {
       composable<SopletterOnboarding> {
           SopletterOnboardingRoute (
               paddingValues = paddingValues,
               navigateToNickname = {}
           )
       }
    }
}