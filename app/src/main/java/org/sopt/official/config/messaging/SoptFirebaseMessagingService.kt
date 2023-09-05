package org.sopt.official.config.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.data.persistence.SoptDataStore
import javax.inject.Inject

@AndroidEntryPoint
class SoptFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var dataStore: SoptDataStore

    override fun onNewToken(token: String) {
        dataStore.pushToken = token
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // sendRegistrationToServer(token)
    }
}
