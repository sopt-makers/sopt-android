package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.PokeRandomUserList

@Serializable
data class PokeRandomUserListResult(
    @SerialName("randomInfoList")
    val randomInfoList: List<PokeRandomUser>
) {
    fun toEntity(): PokeRandomUserList = PokeRandomUserList(
        randomInfoList = randomInfoList.map { it.toEntity() },
    )

    @Serializable
    data class PokeRandomUser(
        @SerialName("randomType")
        val randomType: String,
        @SerialName("randomTitle")
        val randomTitle: String,
        @SerialName("userInfoList")
        val userInfoList: List<PokeUserResult>
    ) {
        fun toEntity(): PokeRandomUserList.PokeRandomUsers = PokeRandomUserList.PokeRandomUsers(
            randomType = randomType,
            randomTitle = randomTitle,
            userInfoList = userInfoList.map { it.toEntity() },
        )
    }
}
