package org.sopt.official.feature.poke.v2.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

/**
 * 콕찌르기 온보딩
 */
@Serializable
data object PokeOnboarding : Route

fun NavController.navigateToPokeOnboarding(navOptions: NavOptions? = null) {
    navigate(PokeOnboarding, navOptions)
}
