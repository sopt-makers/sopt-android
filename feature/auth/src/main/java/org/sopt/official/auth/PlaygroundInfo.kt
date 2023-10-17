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

internal object PlaygroundInfo {
    const val DEBUG_HOST: String = "sopt-internal-dev.pages.dev"
    const val HOST: String = "playground.sopt.org"
    const val AUTH_PATH: String = "auth/oauth"
    const val REDIRECT_URI = "app://org.sopt.makers.android/oauth2redirect"
    const val DEBUG_AUTH_HOST: String = "app.dev.sopt.org/api/v2/"
    const val RELEASE_AUTH_HOST: String = "app.sopt.org/api/v2/"
}
