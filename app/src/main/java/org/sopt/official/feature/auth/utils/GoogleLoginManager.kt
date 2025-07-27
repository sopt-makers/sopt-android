package org.sopt.official.feature.auth.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.official.BuildConfig.serverClientId
import org.sopt.official.common.coroutines.suspendRunCatching
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleLoginManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun getGoogleIdToken(): String {
        var idToken = ""
        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClientId)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        suspendRunCatching {
            credentialManager.getCredential(
                request = credentialRequest,
                context = context
            )
        }.onSuccess {
            if (it.credential is CustomCredential &&
                it.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(it.credential.data)
                idToken = googleIdTokenCredential.idToken
            }
        }.onFailure(Timber::e)

        return idToken
    }
}
