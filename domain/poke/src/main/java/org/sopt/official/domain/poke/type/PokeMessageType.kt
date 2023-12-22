package org.sopt.official.domain.poke.type

enum class PokeMessageType(
    val typeName: String,
) {
    POKE_SOMEONE("pokeSomeone"),
    POKE_FRIEND("pokeFriend"),
    REPLY_NEW("replyNew"),
}