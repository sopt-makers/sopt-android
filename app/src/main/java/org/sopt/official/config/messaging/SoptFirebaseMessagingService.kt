package org.sopt.official.config.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.usecase.notification.RegisterPushTokenUseCase
import javax.inject.Inject

@AndroidEntryPoint
class SoptFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var dataStore: SoptDataStore

    @Inject
    lateinit var registerPushTokenUseCase: RegisterPushTokenUseCase

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onNewToken(token: String) {
        if (dataStore.userStatus == UserStatus.UNAUTHENTICATED.name) return
        scope.launch {
            dataStore.pushToken = token
            registerPushTokenUseCase.invoke(token)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
