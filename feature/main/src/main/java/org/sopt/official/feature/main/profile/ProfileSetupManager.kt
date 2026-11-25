package org.sopt.official.feature.main.profile

import javax.inject.Inject
import kotlinx.coroutines.flow.first
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.model.UserStatus

class ProfileSetupManager @Inject constructor(
    private val userStorage: UserStorage,
) {
    suspend fun consumeProfileSetup(userStatus: UserStatus): Boolean {
        if (userStatus == UserStatus.UNAUTHENTICATED) return false

        val needProfileSetup = userStorage.needProfileSetup.first()
        if (needProfileSetup) userStorage.saveNeedProfileSetup(false)
        return needProfileSetup
    }
}
