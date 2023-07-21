package org.sopt.official.util.ui

import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

object UxConstant {
    const val UI_REACTION_TIME = 300L
}

fun <T : Any> Observable<T>.throttleUi(interval: Long = UxConstant.UI_REACTION_TIME): Flowable<T> = this
    .toFlowable(BackpressureStrategy.DROP)
    .throttleFirst(interval, TimeUnit.MILLISECONDS)