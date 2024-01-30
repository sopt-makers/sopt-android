/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
