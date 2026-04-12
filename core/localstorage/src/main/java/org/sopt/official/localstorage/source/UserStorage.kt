package org.sopt.official.localstorage.source

import kotlinx.coroutines.flow.Flow
import org.sopt.official.model.UserStatus

/**
 * 사용자 관련 로컬 저장소
 * @param userStatus 유저 상태
 * @param pushToken 푸시 토큰
 * @param platform 인증 플랫폼
 * @param saveUserStatus 유저 상태 저장
 * @param savePushToken 푸시 토큰 저장
 * @param savePlatform 인증 플랫폼 저장
 * @param clearUser 유저 정보 초기화
 * */
interface UserStorage {
    val userStatus: Flow<UserStatus>
    val pushToken: Flow<String>
    val platform: Flow<String>

    suspend fun saveUserStatus(status: UserStatus)
    suspend fun savePushToken(token: String)
    suspend fun savePlatform(platform: String)
    suspend fun clearUser()
}