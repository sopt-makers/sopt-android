package org.sopt.official.feature.soptlog.navigation

import androidx.compose.runtime.Stable

@Stable
interface SoptLogNavigation {
    fun navigateToDeepLink(url: String)
    fun navigateToPoke(url: String, isNewPoke: Boolean, currentDestination: Int, friendType: String?)
}