package org.sopt.official.network.authenticator

import android.content.Context
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.network.model.request.ExpiredTokenRequest
import org.sopt.official.network.persistence.SoptDataStore
import org.sopt.official.network.service.RefreshApi
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CentralizeAuthentication @Inject constructor(
    private val dataStore: SoptDataStore,
    private val refreshApi: RefreshApi,
    @ApplicationContext private val context: Context,
    private val navigatorProvider: NavigatorProvider
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 400) {
            val refreshToken = dataStore.refreshToken
            if (refreshToken.isEmpty()) return null
            val newTokens = runCatching {
                runBlocking {
                    refreshApi.refreshToken(
                        ExpiredTokenRequest(
                            accessToken = dataStore.accessToken,
                            refreshToken = refreshToken
                        )
                    )
                }
            }.onSuccess {
                dataStore.accessToken = it.data?.accessToken.orEmpty()
                dataStore.refreshToken = it.data?.accessToken.orEmpty()
            }.onFailure {
                dataStore.clear()
                Timber.e(it)
                ProcessPhoenix.triggerRebirth(context, navigatorProvider.getAuthActivityIntent())
            }.getOrThrow()

            return response.request.newBuilder()
                .header("Authorization", newTokens.data?.accessToken.orEmpty())
                .build()
        }

        return null
    }
}