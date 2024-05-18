package org.sopt.official.domain.soptamp.fake

import org.sopt.official.domain.soptamp.model.ImageUploadUrl
import org.sopt.official.domain.soptamp.repository.ImageUploaderRepository

object FakeImageUploaderRepository : ImageUploaderRepository {
    private val fakeImageUploadUrl = ImageUploadUrl(
        preSignedURL = "",
        imageURL = ""
    )

    override suspend fun uploadImage(preSignedURL: String, imageUri: String) {}
    override suspend fun getS3URL(): Result<ImageUploadUrl> = runCatching { fakeImageUploadUrl }
}
