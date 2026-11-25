package org.sopt.official.feature.main.profile

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.model.UserStatus

class ProfileSetupManagerTest {
    private val userStorage: UserStorage = mockk(relaxed = true)
    private val manager = ProfileSetupManager(userStorage)

    @Test
    fun `플래그가 켜져 있으면 등록 화면을 띄우고 플래그를 해제한다`() = runTest {
        every { userStorage.needProfileSetup } returns flowOf(true)

        assertTrue(manager.consumeProfileSetup(UserStatus.ACTIVE))
        coVerify(exactly = 1) { userStorage.saveNeedProfileSetup(false) }
    }

    @Test
    fun `플래그가 꺼져 있으면 등록 화면을 띄우지 않는다`() = runTest {
        every { userStorage.needProfileSetup } returns flowOf(false)

        assertFalse(manager.consumeProfileSetup(UserStatus.ACTIVE))
    }

    @Test
    fun `비회원이면 등록 화면을 띄우지 않고 플래그를 유지한다`() = runTest {
        every { userStorage.needProfileSetup } returns flowOf(true)

        assertFalse(manager.consumeProfileSetup(UserStatus.UNAUTHENTICATED))
        coVerify(exactly = 0) { userStorage.saveNeedProfileSetup(any()) }
    }
}
