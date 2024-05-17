package org.sopt.official.domain.soptamp.fake

import org.sopt.official.domain.soptamp.repository.ImageUploaderRepository

object FakeImageUploaderRepository : ImageUploaderRepository {
    override suspend fun uploadImage(preSignedURL: String, imageUri: String) {}
}
