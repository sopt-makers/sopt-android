package org.sopt.official.domain.soptamp.repository

interface ImageUploaderRepository {
    suspend fun uploadImage(preSignedURL: String, imageUri: String)
}
