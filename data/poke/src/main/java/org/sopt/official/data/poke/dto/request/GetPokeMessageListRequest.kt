package org.sopt.official.data.poke.dto.request

import org.sopt.official.domain.poke.type.PokeMessageType

data class GetPokeMessageListRequest(
    val messageType: PokeMessageType,
)
