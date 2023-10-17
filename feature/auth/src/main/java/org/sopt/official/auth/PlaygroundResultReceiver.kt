/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
