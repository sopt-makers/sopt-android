package org.sopt.official.data.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.UserActiveState
import org.sopt.official.domain.entity.home.SoptActiveGeneration
import org.sopt.official.domain.entity.home.SoptActiveRecord
import org.sopt.official.domain.entity.home.SoptUser
import org.sopt.official.domain.entity.home.User

@Serializable
data class HomeResponse(
    @SerialName("user")
    val user: HomeUserResponse,
    @SerialName("operation")
    val operation: HomeOperationResponse,
    @SerialName("isAllConfirm")
    val isAllConfirm: Boolean
) {

    fun toEntity(): SoptUser = SoptUser(
        user = this.user.toEntity(),
        operation = this.operation.toEntity(),
        isAllConfirm = this.isAllConfirm
    )

    @Serializable
    data class HomeUserResponse(
        @SerialName("status")
        val status: String,
        @SerialName("name")
        val name: String?,
        @SerialName("profileImage")
        val profileImage: String?,
        @SerialName("generationList")
        val generationList: List<Long>?,
    ) {
        fun toEntity(): User = User(
            activeState = UserActiveState.valueOf(this.status),
            name = this.name,
            profileImage = this.profileImage,
            generationList = SoptActiveGeneration(this.generationList ?: listOf()),
        )
    }

    @Serializable
    data class HomeOperationResponse(
        @SerialName("attendanceScore")
        val attendanceScore: Double?,
        @SerialName("announcement")
        val announcement: String?,
    ) {
        fun toEntity(): SoptActiveRecord = SoptActiveRecord(
            attendanceScore = this.attendanceScore,
            announcement = this.announcement,
        )
    }
}
