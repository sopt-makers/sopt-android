package org.sopt.official.data.soptamp.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.official.common.util.ContentUriRequestBody
import org.sopt.official.data.soptamp.remote.api.S3Service
import org.sopt.official.domain.soptamp.repository.ImageUploaderRepository
import javax.inject.Inject

class ImageUploaderRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: S3Service,
) : ImageUploaderRepository {

    override suspend fun uploadImage(preSignedURL: String, imageUri: String) {
        service.putS3Image(preSignedURL, imageUri.run {
            val uri = Uri.parse(this)
            val requestBody = ContentUriRequestBody(context, uri)
            requestBody
        })
    }
}
