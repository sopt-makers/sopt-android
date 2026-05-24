package org.sopt.official.sopletter.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object SopletterOnboarding

fun NavController.navigateToSopletterOnboarding(
    navOptions: NavOptions? = null,
) {
    navigate(SopletterOnboarding, navOptions)
}

