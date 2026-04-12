package org.sopt.official.localstorage.source

import kotlinx.coroutines.flow.Flow

/**
 * 유저 토큰 관련 로컬 저장소
 * @param accessToken 액세스 토큰
 * @param refreshToken 리프레시 토큰
 * @param playgroundToken 플레이그라운드 토큰
 * @param saveTokens 액세스 토큰과 리프레시 토큰 저장
 * @param savePlaygroundToken 플레이그라운드 토큰 저장
 * @param clearTokens 토큰 초기화
 * */
interface TokenStorage {
    val accessToken: Flow<String>
    val refreshToken: Flow<String>
    val playgroundToken: Flow<String>

    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun savePlaygroundToken(playgroundToken: String)
    suspend fun clearTokens()
}