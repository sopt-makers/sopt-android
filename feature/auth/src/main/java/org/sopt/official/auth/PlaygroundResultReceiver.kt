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