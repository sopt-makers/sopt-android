package org.sopt.official.util.rx

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

fun <T : Any> Flowable<T>.subscribeOnIo() = this.subscribeOn(Schedulers.io())

fun <T : Any> Flowable<T>.observeOnMain() = this.observeOn(AndroidSchedulers.mainThread())

fun <T : Any> Flowable<T>.subscribeBy(
    compositeDisposable: CompositeDisposable,
    onError: (Throwable) -> Unit = { Timber.e(it.message) },
    onComplete: () -> Unit = { },
    onNext: (T) -> Unit
) = compositeDisposable.add(subscribe(onNext, onError, onComplete))
