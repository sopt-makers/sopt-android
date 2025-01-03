package org.sopt.official.feature.auth.authenticator

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.common.coroutines.suspendRunCatching
import timber.log.Timber

@Composable
fun rememberGoogleExecutor(
    onSignInSuccess: (GoogleSignInAccount) -> Unit,
    onSignInFailed: (Throwable) -> Unit = { Timber.e(it) }
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            scope.launch {
                suspendRunCatching {
                    GoogleSignIn.getSignedInAccountFromIntent(data).await()
                }.onSuccess {
                    onSignInSuccess(it)
                }.onFailure {
                    onSignInFailed(it)
                }
            }
        } else {
            Timber.d("Google Sign-In canceled.")
        }
    }

    return launcher
}
