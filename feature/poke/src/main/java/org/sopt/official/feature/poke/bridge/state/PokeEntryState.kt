package org.sopt.official.feature.poke.bridge.state

import androidx.compose.runtime.Immutable

@Immutable
data class PokeEntryState(
    val generation: Int = 0,
    val isNewPoke: Boolean? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
