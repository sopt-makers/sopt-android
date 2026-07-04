package org.sopt.official.feature.sopletter.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

@Serializable
data class SopletterMain(
    val topicId: Long? = null,
) : Route

fun NavController.navigateToSopletterMain(
    topicId: Long? = null,
    navOptions: NavOptions? = null,
) {
    navigate(SopletterMain(topicId = topicId), navOptions)
}
