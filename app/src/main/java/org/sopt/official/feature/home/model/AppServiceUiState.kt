package org.sopt.official.feature.home.model

data class AppServiceUiState(
    val showSoptamp: Boolean = false,
    val showPoke: Boolean = false
)

enum class AppServiceEnum {
    SOPTAMP,
    POKE
}
