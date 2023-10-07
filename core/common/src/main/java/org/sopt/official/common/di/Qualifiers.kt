package org.sopt.official.common.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Logging

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Auth(val needed: Boolean)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppRetrofit(val authNeeded: Boolean)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OperationRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalStore
