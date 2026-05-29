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
package org.sopt.official.config

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import org.sopt.official.domain.notification.usecase.RegisterPushTokenUseCase
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.model.UserStatus
import javax.inject.Inject
import javax.inject.Singleton

/**
 * FCM 토큰 조회, 로컬 저장, 서버 등록을 앱 전역에서 일관되게 처리하는 Manger class
 */
@Singleton
class FcmPushTokenManager @Inject constructor(
    private val registerPushTokenUseCase: RegisterPushTokenUseCase,
    private val userStorage: UserStorage,
) {
    /**
     * 현재 기기의 FCM 토큰을 조회하고 서버에 등록한다.
     *
     * 로그인 성공처럼 인증 토큰 저장이 완료된 직후 현재 FCM 토큰을 서버와 동기화해야 할 때 사용한다.
     */
    suspend fun registerCurrentToken() {
        val token = FirebaseMessaging.getInstance().token.await()
        saveAndRegisterToken(token)
    }

    /**
     * Firebase에서 새로 발급한 FCM 토큰을 인증 유저인 경우 서버에 재등록한다.
     *
     * FCM 토큰 갱신 콜백에서 호출한다.
     */
    suspend fun registerPushTokenIfAuthenticated(token: String) {
        if (userStorage.userStatus.first() == UserStatus.UNAUTHENTICATED) return

        saveAndRegisterToken(token)
    }

    /**
     * 전달받은 FCM 토큰을 로컬에 저장한 뒤 서버에 등록한다.
     */
    private suspend fun saveAndRegisterToken(token: String) {
        userStorage.savePushToken(token)
        registerPushTokenUseCase(pushToken = token)
    }
}
