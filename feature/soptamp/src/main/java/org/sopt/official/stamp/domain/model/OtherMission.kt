package org.sopt.official.stamp.domain.model

data class OtherMission(
    val nickname: String,
    val profileMessage: String,
    val userMissions: List<Mission>
) {
    data class Mission(
        val id: Int,
        val title: String,
        val level: Int,
        val display: Boolean,
        val profileImage: String? = null,
    )
}
