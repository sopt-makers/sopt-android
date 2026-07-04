package org.sopt.official.data.user.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.user.model.WithdrawModel

@Serializable
data class WithdrawResponseDto(
    @SerialName("withdrawFormUrl")
    val withdrawFormUrl: String,
) {
    fun toDomain() = WithdrawModel(
        withdrawFormUrl = withdrawFormUrl
    )
}
