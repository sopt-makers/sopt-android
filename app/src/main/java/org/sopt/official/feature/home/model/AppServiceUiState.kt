package org.sopt.official.feature.home.model

data class AppServiceUiState(
    val showSoptamp: Boolean = false,
    val showPoke: Boolean = false,
    val showHotboard: Boolean = false
)

enum class AppServiceEnum {
    SOPTAMP,
    POKE,
    HOTBOARD
}
