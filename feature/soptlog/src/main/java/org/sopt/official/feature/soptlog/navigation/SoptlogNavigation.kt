package org.sopt.official.feature.soptlog.navigation

import androidx.compose.runtime.Stable

@Stable
sealed interface SoptlogNavigation {

    @Stable
    interface SoptlogAppServiceNavigation : SoptlogNavigation {
        fun navigateToDeepLink(url: String)
        fun navigateToPoke(url: String, isNewPoke: Boolean, currentDestination: Int, friendType: String?)
    }
}