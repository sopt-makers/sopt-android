package org.sopt.official.domain.poke.entity

class GetPokeFriendListResponse: BaseResponse<List<PokeFriend>>()

data class PokeFriend(
    val userId: Long,
    val profileImage: String,
    val name: String,
    val generation: Int,
    val part: String,
    val pokeNum: Int,
    val isAlreadyPoke: Boolean
)
