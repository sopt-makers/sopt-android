package org.sopt.official.domain.home.result

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

inline fun <T, R> T.runCatching(block: T.() -> R): Result<R> = try {
    Result.Success(block())
} catch (e: Throwable) {
    Result.Error(e)
}
