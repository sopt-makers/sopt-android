package org.sopt.official.auth

internal object Constants {
    const val NOT_SECURE_SCHEME: String = "http"
    const val SCHEME: String = "https"
    const val REDIRECT_URI: String = "redirect_uri"
    const val CODE: String = "code"
    const val STATE: String = "state"
    const val EXCEPTION: String = "exception"
    const val RESULT_RECEIVER: String = "resultReceiver"

    const val AUTH_INTENT_BUNDLE_KEY = "auth.intent.bundle.key"
    const val AUTH_URI_KEY = "auth.uri.key"
}