package org.sopt.official.domain.soptamp.repository

import org.sopt.official.domain.soptamp.model.ImageUploadUrl

interface ImageUploaderRepository {
    suspend fun uploadImage(preSignedURL: String, imageUri: String)
    suspend fun getImageUploadURL(): Result<ImageUploadUrl>
}
