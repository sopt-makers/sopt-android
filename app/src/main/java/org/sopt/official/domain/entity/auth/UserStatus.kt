package org.sopt.official.domain.entity.auth

enum class UserStatus(
    val value: String
) {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    UNAUTHENTICATED("UNAUTHENTICATED");

    companion object {
        fun of(value: String) = entries.find { it.value == value } ?: throw IllegalArgumentException("Invalid user status: $value")
    }
}
