package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.model.S3URL

@Serializable
data class S3URLResponse(
    @SerialName("preSignedURL")
    val preSignedURL: String,
    @SerialName("imageURL")
    val imageURL: String
) {
    fun toDomain() = S3URL(
        preSignedURL = preSignedURL,
        imageURL = imageURL
    )
}
