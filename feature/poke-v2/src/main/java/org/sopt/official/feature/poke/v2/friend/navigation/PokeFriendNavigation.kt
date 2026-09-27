package org.sopt.official.feature.poke.v2.friend.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

/**
 * 내 친구
 */
@Serializable
data object PokeFriend : Route

fun NavController.navigateToPokeFriend(navOptions: NavOptions? = null) {
    navigate(PokeFriend, navOptions)
}
