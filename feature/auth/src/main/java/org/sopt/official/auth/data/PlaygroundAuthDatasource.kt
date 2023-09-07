package org.sopt.official.auth.data

import org.sopt.official.auth.data.remote.model.response.OAuthToken

interface PlaygroundAuthDatasource {
    suspend fun oauth(code: String): Result<OAuthToken>
}