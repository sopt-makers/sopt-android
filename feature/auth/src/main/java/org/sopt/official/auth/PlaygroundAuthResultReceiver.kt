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

import android.os.Bundle
import org.sopt.official.playground.auth.utils.getParcelableAs

internal class PlaygroundAuthResultReceiver(
    callback: (String?, Throwable?) -> Unit
) : PlaygroundResultReceiver<(state: String?, error: Throwable?) -> Unit>() {
    init {
        super.emitter = callback
    }

    override fun receiveSuccess(resultData: Bundle?) {
        resultData?.getString(Constants.STATE)?.let {
            emitter?.invoke(it, null)
        } ?: emitter?.invoke(null, PlaygroundError.NotFoundStateCode())
    }

    override fun receiveFailure(resultData: Bundle?) {
        val error = resultData?.getParcelableAs<PlaygroundError>(Constants.EXCEPTION)
        emitter?.invoke(null, error)
    }

    override fun error() {
        emitter?.invoke(null, PlaygroundError.IllegalStateCode())
    }
}
