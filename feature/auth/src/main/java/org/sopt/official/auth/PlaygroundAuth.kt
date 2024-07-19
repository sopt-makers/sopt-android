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
import org.sopt.official.auth.utils.PlaygroundUriProvider
import org.sopt.official.network.model.response.OAuthToken

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

    private fun startAuthTab(context: Context, uri: Uri, state: String, resultReceiver: ResultReceiver) {
        AuthIntentFactory.authIntentWithAuthTab(
            context = context,
            uri = uri,
            state = state,
            resultReceiver = resultReceiver
        ).run { context.startActivity(this) }
    }

    private fun resultReceiver(authDataSource: PlaygroundAuthDatasource, callback: (Result<OAuthToken>) -> Unit) =
        PlaygroundAuthResultReceiver(
            callback = { code, error ->
                if (error != null) {
                    callback(Result.failure(error as PlaygroundError))
                } else {
                    code ?: throw IllegalStateException()
                    requestOauthToken(authDataSource, code, callback)
                }
            }
        )

    private fun requestOauthToken(authDataSource: PlaygroundAuthDatasource, code: String, callback: (Result<OAuthToken>) -> Unit) =
        CoroutineScope(Dispatchers.Default).launch {
            callback(authDataSource.oauth(code))
        }
}
