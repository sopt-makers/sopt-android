/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
