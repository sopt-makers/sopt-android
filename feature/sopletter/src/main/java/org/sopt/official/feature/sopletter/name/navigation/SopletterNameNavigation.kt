package org.sopt.official.feature.sopletter.name.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

@Serializable
data class SopletterName(
    val nickname: String,
    val generation: Int
): Route

fun NavController.navigateToSopletterName(
    nickname: String,
    generation: Int,
    navOptions: NavOptions? = null,
) {
    navigate(SopletterName(nickname, generation), navOptions)
}