package org.sopt.official.domain.poke.entity

data class PokeNotificationItem (
    val userId: Long,
    val profileImage: String,
    val name: String,
    val message: String,
    val generation: Int,
    val part: String,
    val pokeNum: Int,
    val relationName: String,
    val mutual: List<String>,
    val isFirstMeet: Boolean,
    val isAlreadyPoke: Boolean
)