package org.sopt.official.util.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

fun <T : Any> Single<T>.subscribeOnIo() = this.subscribeOn(Schedulers.io())

fun <T : Any> Single<T>.observeOnMain() = this.observeOn(AndroidSchedulers.mainThread())

fun <T : Any> Single<T>.subscribeBy(
    compositeDisposable: CompositeDisposable,
    onError: (Throwable) -> Unit = { Timber.e(it.message) },
    onSuccess: (T) -> Unit
) = compositeDisposable.add(subscribe(onSuccess, onError))