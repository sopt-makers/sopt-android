package org.sopt.official.feature.sopletter.write.model

data class SopletterWriteUiState(
    val writerName: String = "",

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)