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
