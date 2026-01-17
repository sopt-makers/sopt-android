/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.auth.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import org.sopt.official.common.BuildConfig
import org.sopt.official.common.coroutines.suspendRunCatching
import timber.log.Timber
import dev.zacsweers.metro.Inject
import javax.inject.Singleton

@Singleton
@Inject
class GoogleLoginManager() {
    suspend fun getGoogleIdToken(context: Context): String {
        var idToken = ""
        val clientId = if (BuildConfig.DEBUG) BuildConfig.DEV_SERVER_CLIENT_ID else BuildConfig.PROD_SERVER_CLIENT_ID
        val credentialManager = CredentialManager.create(context)
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
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
