package org.sopt.official.util.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import org.sopt.official.util.wrapper.NullableWrapper
import org.sopt.official.util.wrapper.asNullableWrapper

fun <ValueType : Any> Flow<NullableWrapper<ValueType>>.stateInLazily(
    scope: CoroutineScope
) = this.stateIn(
    scope,
    SharingStarted.Lazily,
    NullableWrapper.none()
)

fun <ValueType : Any> Flow<ValueType>.stateInLazily(
    scope: CoroutineScope,
    initialValue: ValueType
) = this.stateIn(
    scope,
    SharingStarted.Lazily,
    initialValue
)

fun <ValueType : Any> Flow<ValueType>.safeStateInLazily(
    scope: CoroutineScope,
) = this.map { it.asNullableWrapper() }
    .stateIn(
        scope,
        SharingStarted.Lazily,
        NullableWrapper.none()
    )