package org.sopt.official.core.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

inline fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    crossinline block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}
