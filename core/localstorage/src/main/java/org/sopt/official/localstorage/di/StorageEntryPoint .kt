package org.sopt.official.localstorage.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.official.localstorage.source.TokenStorage
import org.sopt.official.localstorage.source.UserStorage

@EntryPoint
@InstallIn(SingletonComponent::class)
interface StorageEntryPoint {
    fun userStorage(): UserStorage
    fun tokenStorage(): TokenStorage
}