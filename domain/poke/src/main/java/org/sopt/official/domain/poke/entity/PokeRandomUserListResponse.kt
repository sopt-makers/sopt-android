package org.sopt.official.domain.poke.entity

data class PokeRandomUserList(
    val randomInfoList: List<PokeRandomUsers>
) {
    data class PokeRandomUsers(
        val randomType: String,
        val randomTitle: String,
        val userInfoList: List<PokeUser>
    )
}
