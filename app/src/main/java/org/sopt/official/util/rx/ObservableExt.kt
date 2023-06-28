package org.sopt.official.util.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


fun <T : Any> Observable<T>.subscribeOnIo() = this.subscribeOn(Schedulers.io())

fun <T : Any> Observable<T>.observeOnMain() = this.observeOn(AndroidSchedulers.mainThread())

fun <T : Any> Observable<T>.subscribeBy(
    onError: (Throwable) -> Unit = { Timber.e(it.message) },
    onComplete: () -> Unit = { Unit },
    onNext: (T) -> Unit
): Disposable = subscribe(onNext, onError, onComplete)