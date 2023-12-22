package org.sopt.official.domain.poke.entity

sealed interface ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class ApiError(val statusCode: String, val responseMessage: String) : ApiResult<Nothing>
    data class Failure(val throwable: Throwable) : ApiResult<Nothing>
}

suspend fun <T> ApiResult<T>.onSuccess(action: suspend (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        action.invoke(this.data)
    }
    return this
}

suspend fun <T> ApiResult<T>.onApiError(
    action: suspend (
        statusCode: String,
        responseMessage: String,
    ) -> Unit
): ApiResult<T> {
    if (this is ApiResult.ApiError) {
        action.invoke(this.statusCode, this.responseMessage)
    }
    return this
}

suspend fun <T> ApiResult<T>.onFailure(action: suspend (Throwable) -> Unit): ApiResult<T> {
    if (this is ApiResult.Failure) {
        action.invoke(this.throwable)
    }
    return this
}

@Suppress("UNCHECKED_CAST")
inline fun <T : Any> apiResult(call: () -> BaseResponse<T>): ApiResult<T> {
    return runCatching {
        val response = call.invoke()
        if (response.statusCode.toInt() in 200..299) {
            ApiResult.Success(response.data)
        } else {
            ApiResult.ApiError(response.statusCode, response.responseMessage)
        }
    }.getOrElse {
        ApiResult.Failure(it)
    } as ApiResult<T>
}
