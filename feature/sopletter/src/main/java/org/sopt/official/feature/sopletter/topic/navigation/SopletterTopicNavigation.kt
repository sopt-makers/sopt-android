package org.sopt.official.feature.sopletter.topic.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

@Serializable
data object SopletterTopic : Route

fun NavController.navigateToSopletterTopic(
    navOptions: NavOptions? = null,
) {
    navigate(SopletterTopic, navOptions)
}
