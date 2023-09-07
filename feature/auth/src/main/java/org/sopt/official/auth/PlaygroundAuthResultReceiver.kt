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