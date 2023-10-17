/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
