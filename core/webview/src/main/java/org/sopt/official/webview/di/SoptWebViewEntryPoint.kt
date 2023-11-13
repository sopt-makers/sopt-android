package org.sopt.official.webview.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.network.persistence.SoptDataStore

@InstallIn(SingletonComponent::class)
@EntryPoint
interface SoptWebViewEntryPoint {
    fun dataStore(): SoptDataStore
}
