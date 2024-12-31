package org.sopt.official.feature.mypage.soptamp.state

import androidx.compose.runtime.Stable

@Stable
data class ModifySoptampProfileUiState(
    val current: String,
    val previous: String,
    val onChangeCurrent: (String) -> Unit,
    val onChangePrevious: (String) -> Unit,
) {
    val isConfirmed: Boolean
        get() = current != previous
}
