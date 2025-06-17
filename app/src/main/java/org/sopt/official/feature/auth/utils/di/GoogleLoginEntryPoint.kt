package org.sopt.official.feature.auth.utils.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.feature.auth.utils.GoogleLoginManager

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GoogleLoginManagerEntryPoint {
    fun googleLoginManager(): GoogleLoginManager
}
