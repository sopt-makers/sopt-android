package org.sopt.official.feature.poke

sealed class UiState<out T : Any> {
    data object Loading : UiState<Nothing>()
    data class Success<out T : Any>(
        val data: T
    ) : UiState<T>()

    data class ApiError(
        val statusCode: String,
        val responseMessage: String,
    ) : UiState<Nothing>()

    data class Failure(
        val throwable: Throwable
    ) : UiState<Nothing>()
}