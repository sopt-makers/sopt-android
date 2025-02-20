package org.sopt.official.feature.home.model

sealed interface HomeEvent {
    interface HomeShortcutEvent : HomeEvent {
        fun navigateToPlayground()
        fun navigateToPlaygroundGroup()
        fun navigateToPlaygroundMember()
        fun navigateToPlaygroundProject()
        fun navigateToSoptHomepage()
        fun navigateToSoptReview()
        fun navigateToSoptProject()
        fun navigateToSoptInstagram()
    }

    interface HomeDashboardEvent : HomeEvent {
        fun navigateToNotification()
        fun navigateToSetting()
        fun navigateToSchedule()
        fun navigateToSoptlog()
        fun navigateToAttendance()
    }
}
