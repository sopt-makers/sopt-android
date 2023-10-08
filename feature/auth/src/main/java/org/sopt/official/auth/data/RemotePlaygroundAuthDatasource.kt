package org.sopt.official.auth.data

import org.sopt.official.auth.PlaygroundError
import org.sopt.official.auth.data.remote.AuthService
import org.sopt.official.auth.data.remote.model.request.RequestToken
import org.sopt.official.auth.data.remote.model.response.OAuthToken
import org.sopt.official.common.di.Auth
import java.net.UnknownHostException

internal class RemotePlaygroundAuthDatasource(
    @Auth private val authService: AuthService
) : PlaygroundAuthDatasource {

    override suspend fun oauth(code: String): Result<OAuthToken> {
        val result = kotlin.runCatching { authService.oauth(RequestToken(code)) }
        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is UnknownHostException -> Result.failure(PlaygroundError.NetworkUnavailable())
            else -> Result.failure(exception)
        }
    }
}