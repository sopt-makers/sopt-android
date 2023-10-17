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
