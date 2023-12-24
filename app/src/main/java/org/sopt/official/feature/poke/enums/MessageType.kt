package org.sopt.official.feature.poke.enums

enum class MessageType(
    val messageType: String
) {
    REPLY_NEW("replyNew"),
    POKE_SOMEONE("pokeSomeone"),
    POKE_FRIEND("pokeFriend")
}