package org.sopt.official.feature.soptlog.state

sealed interface SoptlogNavigationEvent {
    data class NavigateToPoke(
        val url: String,
        val isNewPoke: Boolean,
        val friendType: String?
    ) : SoptlogNavigationEvent

    data class NavigateToDeepLink(val url: String) : SoptlogNavigationEvent
}