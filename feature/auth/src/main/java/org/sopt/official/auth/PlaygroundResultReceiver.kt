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

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import org.sopt.official.auth.utils.PlaygroundLog

abstract class PlaygroundResultReceiver<T> : ResultReceiver(Handler(Looper.getMainLooper())) {
    var emitter: T? = null

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        when (resultCode) {
            Activity.RESULT_OK -> receiveOK(resultData)
            Activity.RESULT_CANCELED -> receiveCanceled(resultData)
            else -> error()
        }
        emitter = null
    }

    private fun receiveOK(resultData: Bundle?) {
        PlaygroundLog.d("Auth resultCode: OK")
        logResultData(resultData)
        receiveSuccess(resultData)
    }

    private fun receiveCanceled(resultData: Bundle?) {
        PlaygroundLog.d("Auth resultCode: Canceled")
        logResultData(resultData)
        receiveFailure(resultData)
    }

    private fun logResultData(resultData: Bundle?) =
        PlaygroundLog.d("Auth State Result Data: $resultData")

    abstract fun receiveSuccess(resultData: Bundle?)

    abstract fun receiveFailure(resultData: Bundle?)

    abstract fun error()
}
