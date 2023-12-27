package org.sopt.official.domain.poke.type

enum class PokeMessageType(
    val typeName: String,
) {
    REPLY_NEW("replyNew"),
    POKE_SOMEONE("pokeSomeone"),
    POKE_FRIEND("pokeFriend")
}