package org.sopt.official.feature.soptlog.state

sealed interface SoptLogNavigationEvent {
    data class NavigateToPoke(
        val url: String,
        val isNewPoke: Boolean,
        val friendType: String?
    ) : SoptLogNavigationEvent

    data class NavigateToDeepLink(val url: String) : SoptLogNavigationEvent
}