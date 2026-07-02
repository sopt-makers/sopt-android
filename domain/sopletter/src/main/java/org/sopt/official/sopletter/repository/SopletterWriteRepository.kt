package org.sopt.official.sopletter.repository

import org.sopt.official.sopletter.model.Sopletter

interface SopletterWriteRepository {
    suspend fun postSopletter(topicId: Long?, content: String): Result<Sopletter>
}