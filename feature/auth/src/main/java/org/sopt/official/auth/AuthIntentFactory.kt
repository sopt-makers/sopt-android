package org.sopt.official.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver

internal object AuthIntentFactory {
    fun authIntentWithAuthTab(
        context: Context,
        uri: Uri,
        state: String,
        resultReceiver: ResultReceiver
    ): Intent = Bundle().apply {
        putParcelable(Constants.AUTH_URI_KEY, uri)
        putString(Constants.STATE, state)
        putParcelable(Constants.RESULT_RECEIVER, resultReceiver)
    }.run {
        Intent(context, AuthCodeHandlerActivity::class.java)
            .putExtra(Constants.AUTH_INTENT_BUNDLE_KEY, this)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}