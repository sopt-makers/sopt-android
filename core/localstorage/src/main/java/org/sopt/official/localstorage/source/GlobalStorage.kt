package org.sopt.official.localstorage.source

/**
 * 전체 정보 초기화
 * @param clearAll 모든 저장 정보 초기화
 * */
interface GlobalStorage {
    suspend fun clearAll()
}