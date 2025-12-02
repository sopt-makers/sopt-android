package org.sopt.official.feature.poke.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateToPokeEntry(
    navOptions: NavOptions? = null
) {
    navigate(PokeEntry, navOptions)
}

fun NavController.navigateToPokeMain(
    navOptions: NavOptions? = null
) {
    navigate(PokeMain, navOptions)
}

fun NavController.navigateToPokeOnboarding(
    generation: Int,
    userStatus: String
) {
    navigate(PokeOnboarding(generation, userStatus))
}

fun NavController.navigateToPokeNotification(
    userStatus: String,
    navOptions: NavOptions? = null
) {
    navigate(PokeNotification(userStatus), navOptions)
}

fun NavController.navigateToPokeFriendList(
    initialFriendType: String?,
    navOptions: NavOptions? = null
) {
    navigate(PokeFriendList(initialFriendType), navOptions)
}