package org.sopt.official.sopletter.repository

import org.sopt.official.sopletter.model.SopletterWrite


interface SopletterWriteRepository {
    suspend fun postSopletter(topicId: Long?, content: String): Result<SopletterWrite>
}