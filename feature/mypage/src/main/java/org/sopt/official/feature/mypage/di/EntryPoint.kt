package org.sopt.official.feature.mypage.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import org.sopt.official.auth.repository.AuthRepository
import org.sopt.official.common.context.appContext

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface AuthEntryPoint {
    fun authRepository(): AuthRepository
}

internal val authRepository by lazy {
    EntryPointAccessors
        .fromApplication(appContext, AuthEntryPoint::class.java)
        .authRepository()
}
