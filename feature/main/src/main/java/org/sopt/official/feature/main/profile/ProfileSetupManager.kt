package org.sopt.official.feature.main.profile

import javax.inject.Inject
import kotlinx.coroutines.flow.first
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.model.UserStatus

class ProfileSetupManager @Inject constructor(
    private val userStorage: UserStorage,
) {
    suspend fun shouldOpenProfileSetup(userStatus: UserStatus): Boolean {
        if (userStatus == UserStatus.UNAUTHENTICATED) return false
        return userStorage.needProfileSetup.first()
    }

    suspend fun handleWebViewResult(appSchemeUrl: String?) {
        if (appSchemeUrl != PROFILE_COMPLETE_URL) return
        userStorage.saveNeedProfileSetup(false)
    }

    companion object {
        const val PROFILE_COMPLETE_URL = "sopt://profile/complete"
    }
}
