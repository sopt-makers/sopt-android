package org.sopt.official.security.model

data class EncryptedContent(
    val initializationVector: ByteArray,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is EncryptedContent -> false
        !initializationVector.contentEquals(other.initializationVector) -> false
        !data.contentEquals(other.data) -> false
        else -> true
    }

    override fun hashCode(): Int = HASH_PRIME * initializationVector.contentHashCode() + data.contentHashCode()

    fun concatenate() = initializationVector + data

    companion object {
        private const val HASH_PRIME = 31
    }
}