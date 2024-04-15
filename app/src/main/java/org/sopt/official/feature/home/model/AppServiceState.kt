package org.sopt.official.feature.home.model

data class AppServiceState(
    val showSoptamp: Boolean = false,
    val showPoke: Boolean = false
)

enum class AppServiceEnum {
    SOPTAMP, POKE
}
