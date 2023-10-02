package org.sopt.official.common.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Logging

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Auth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OperationRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalStore
