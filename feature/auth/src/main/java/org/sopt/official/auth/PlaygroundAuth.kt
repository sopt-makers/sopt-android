/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.auth

import android.content.Context
import android.net.Uri
import android.os.ResultReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.official.auth.data.PlaygroundApiManager
import org.sopt.official.auth.data.PlaygroundAuthDatasource
import org.sopt.official.auth.data.RemotePlaygroundAuthDatasource
import org.sopt.official.auth.data.remote.model.response.OAuthToken
import org.sopt.official.auth.utils.PlaygroundUriProvider

object PlaygroundAuth {
    fun authorizeWithWebTab(
        context: Context,
        authStateGenerator: AuthStateGenerator = DefaultAuthStateGenerator(),
        uri: Uri? = null,
        isDebug: Boolean = false,
        authDataSource: PlaygroundAuthDatasource = RemotePlaygroundAuthDatasource(
            PlaygroundApiManager.getInstance(isDebug).provideAuthService()
        ),
        callback: (Result<OAuthToken>) -> Unit,
    ) {
        val stateToken = authStateGenerator.generate()
        val resultReceiver = resultReceiver(authDataSource) {
            callback(it)
        }
        startAuthTab(
            context = context,
            uri = uri ?: PlaygroundUriProvider().authorize(state = stateToken, isDebug = isDebug),
            state = stateToken,
            resultReceiver = resultReceiver
        )
    }

    private fun startAuthTab(
        context: Context,
        uri: Uri,
        state: String,
        resultReceiver: ResultReceiver
    ) {
        AuthIntentFactory.authIntentWithAuthTab(
            context = context,
            uri = uri,
            state = state,
            resultReceiver = resultReceiver
        ).run { context.startActivity(this) }
    }

    private fun resultReceiver(
        authDataSource: PlaygroundAuthDatasource,
        callback: (Result<OAuthToken>) -> Unit
    ) = PlaygroundAuthResultReceiver(
        callback = { code, error ->
            if (error != null) {
                callback(Result.failure(error as PlaygroundError))
            } else {
                code ?: throw IllegalStateException()
                requestOauthToken(authDataSource, code, callback)
            }
        }
    )

    private fun requestOauthToken(
        authDataSource: PlaygroundAuthDatasource,
        code: String,
        callback: (Result<OAuthToken>) -> Unit
    ) = CoroutineScope(Dispatchers.Default).launch {
        callback(authDataSource.oauth(code))
    }
}
