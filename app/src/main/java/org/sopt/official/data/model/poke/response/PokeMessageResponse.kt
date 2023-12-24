package org.sopt.official.data.model.poke.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokeMessageResponse (
    @SerialName("messages")
    val messages: List<Message>
) {
    @Serializable
    data class Message(
        @SerialName("messageId")
        val messageId: Long,
        @SerialName("content")
        val content: String
    )
}