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
