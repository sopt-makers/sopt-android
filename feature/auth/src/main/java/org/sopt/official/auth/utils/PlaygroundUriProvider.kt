package org.sopt.official.auth.utils

import android.net.Uri
import org.sopt.official.auth.Constants
import org.sopt.official.auth.PlaygroundInfo

internal class PlaygroundUriProvider {
    fun authorize(
        isDebug: Boolean = false,
        redirectUri: String = PlaygroundInfo.REDIRECT_URI,
        state: String
    ): Uri = Uri.Builder()
        .scheme(Constants.SCHEME)
        .authority(if (isDebug) PlaygroundInfo.DEBUG_HOST else PlaygroundInfo.HOST)
        .path(PlaygroundInfo.AUTH_PATH)
        .appendQueryParameter(Constants.REDIRECT_URI, redirectUri)
        .appendQueryParameter(Constants.STATE, state)
        .build()
}