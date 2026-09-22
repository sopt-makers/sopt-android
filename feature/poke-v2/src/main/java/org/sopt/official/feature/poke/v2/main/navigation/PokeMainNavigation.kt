package org.sopt.official.feature.poke.v2.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

/**
 * 콕찌르기 메인
 */
@Serializable
data object PokeMain : Route

fun NavController.navigateToPokeMain(navOptions: NavOptions? = null) {
    navigate(PokeMain, navOptions)
}
