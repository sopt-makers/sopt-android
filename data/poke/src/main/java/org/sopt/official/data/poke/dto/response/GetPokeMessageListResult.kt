package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.PokeMessageList

@Serializable
data class GetPokeMessageListResult(
    @SerialName("header")
    val header: String,
    @SerialName("messages")
    val messages: List<PokeMessageResult>,
) {
    fun toEntity(): PokeMessageList = PokeMessageList(
        header = header,
        messages = messages.map { it.toEntity() },
    )

    @Serializable
    data class PokeMessageResult(
        @SerialName("messageId")
        val messageId: Int,
        @SerialName("content")
        val content: String,
    ) {
        fun toEntity(): PokeMessageList.PokeMessage = PokeMessageList.PokeMessage(
            messageId = messageId,
            content = content,
        )
    }
}