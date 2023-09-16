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