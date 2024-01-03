package org.sopt.official.data.poke.di


import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Poke

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Strings(val value: Constant)

enum class Constant {
    POKE_DATA_STORE
}