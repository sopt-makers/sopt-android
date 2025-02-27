package org.sopt.official.feature.home.model

import androidx.compose.runtime.Stable

@Stable
sealed interface HomeNavigation {

    @Stable
    interface HomeShortcutNavigation : HomeNavigation {
        fun navigateToPlayground()
        fun navigateToPlaygroundGroup()
        fun navigateToPlaygroundMember()
        fun navigateToPlaygroundProject()
        fun navigateToSoptHomepage()
        fun navigateToSoptReview()
        fun navigateToSoptProject()
        fun navigateToSoptInstagram()
    }

    @Stable
    interface HomeDashboardNavigation : HomeNavigation {
        fun navigateToNotification()
        fun navigateToSetting()
        fun navigateToSchedule()
        fun navigateToSoptlog()
        fun navigateToAttendance()
    }

    @Stable
    interface HomeAppServicesNavigation : HomeNavigation {
        fun navigateToDeepLink(url: String)
    }
}
