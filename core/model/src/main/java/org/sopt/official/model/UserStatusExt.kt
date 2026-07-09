package org.sopt.official.model

fun UserStatus.toViewType(): String = when (this) {
    UserStatus.ACTIVE -> "active"
    UserStatus.INACTIVE -> "inactive"
    UserStatus.UNAUTHENTICATED -> "visitor"
}
