package org.sopt.official.data.soptamp.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import org.sopt.official.common.util.ContentUriRequestBody
import org.sopt.official.data.soptamp.remote.api.S3Service
import org.sopt.official.data.soptamp.remote.api.StampService
import org.sopt.official.domain.soptamp.model.ImageUploadUrl
import org.sopt.official.domain.soptamp.repository.ImageUploaderRepository

class ImageUploaderRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val s3Service: S3Service,
    private val stampService: StampService
) : ImageUploaderRepository {

    override suspend fun uploadImage(preSignedURL: String, imageUri: String) {
        s3Service.putS3Image(
            preSignedURL,
            imageUri.run {
                val uri = Uri.parse(this)
                val requestBody = ContentUriRequestBody(context, uri)
                requestBody
            }
        )
    }

    override suspend fun getS3URL(): Result<ImageUploadUrl> {
        return runCatching { stampService.getS3URL().toDomain() }
    }
}
