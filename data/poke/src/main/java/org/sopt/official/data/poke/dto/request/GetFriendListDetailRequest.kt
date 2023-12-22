package org.sopt.official.data.poke.dto.request

import org.sopt.official.domain.poke.type.PokeFriendType

data class GetFriendListDetailRequest(
    val type: PokeFriendType,
    val page: Int,
)
