package org.sopt.official.domain.poke.entity

class GetPokeMessageListResponse : BaseResponse<PokeMessageList>()

data class PokeMessageList(
    val messages: List<PokeMessage>,
) {
    data class PokeMessage(
        val messageId: Int,
        val content: String,
    )
}