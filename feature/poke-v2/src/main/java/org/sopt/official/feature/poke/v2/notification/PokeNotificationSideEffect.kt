package org.sopt.official.feature.poke.v2.notification

import org.sopt.official.feature.poke.v2.model.PokeRelationChangeState

sealed interface PokeNotificationSideEffect {
    data class ShowLottie(val state: PokeRelationChangeState) : PokeNotificationSideEffect
    data class ShowSuccessSnackbar(val message: String) : PokeNotificationSideEffect
    data class ShowWarningSnackbar(val message: String) : PokeNotificationSideEffect
    data class ShowErrorSnackbar(val message: String) : PokeNotificationSideEffect
}
