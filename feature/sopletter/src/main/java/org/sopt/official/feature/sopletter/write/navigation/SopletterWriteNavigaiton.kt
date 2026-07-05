package org.sopt.official.feature.sopletter.write.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route

@Serializable
data class SopletterWrite(
    val topicId: Long? = null,
) : Route

fun NavController.navigateToSopletterWrite(
    topicId: Long? = null,
    navOptions: NavOptions? = null,
) {
    navigate(SopletterWrite(topicId = topicId), navOptions)
}