package org.sopt.official.data.authenticator

import android.content.Context
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.sopt.official.common.di.Auth
import org.sopt.official.data.model.request.RefreshRequest
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.data.service.AuthService
import org.sopt.official.feature.auth.AuthActivity
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoptAuthenticator @Inject constructor(
    private val dataStore: SoptDataStore,
    @Auth(false) private val service: AuthService,
    @ApplicationContext private val context: Context
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            val refreshToken = dataStore.refreshToken
            val newTokens = runCatching {
                runBlocking {
                    service.refresh(RefreshRequest(refreshToken))
                }
            }.onSuccess {
                dataStore.refreshToken = it.refreshToken
                dataStore.accessToken = it.accessToken
                dataStore.playgroundToken = it.playgroundToken
            }.onFailure {
                dataStore.clear()
                Timber.e(it)
                ProcessPhoenix.triggerRebirth(context, AuthActivity.newInstance(context))
            }.getOrThrow()

            return response.request.newBuilder()
                .header("Authorization", newTokens.accessToken)
                .build()
        }
        return null
    }
}
