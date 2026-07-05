package org.sopt.official.feature.sopletter.print.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.Route
import org.sopt.official.feature.sopletter.write.navigation.SopletterWrite

@Serializable
data class SopletterPrint(
    val topicId: Long? = null,
) : Route

fun NavController.navigateToSopletterPrint(
    topicId: Long? = null,
    navOptions: NavOptions? = null,
) {
    navigate(SopletterPrint(topicId = topicId), navOptions)
}