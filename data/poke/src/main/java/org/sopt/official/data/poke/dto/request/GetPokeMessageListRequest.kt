package org.sopt.official.domain.poke.entity.request

import org.sopt.official.domain.poke.type.PokeMessageType

data class GetPokeMessageListRequest(
    val messageType: PokeMessageType,
)
