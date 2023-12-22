package org.sopt.official.domain.poke.entity

class PokeUserResponse : BaseResponse<PokeUser>()

data class PokeUser(
    val userId: Int,
    val playGroundId: Int,
    val profileImage: String?,
    val name: String,
    val message: String,
    val generation: Int,
    val part: String,
    val pokeNum: Int,
    val relationName: String,
    val mutual: List<PokeUser>,
    val isFirstMeet: Boolean,
    val isAlreadyPoke: Boolean,
)