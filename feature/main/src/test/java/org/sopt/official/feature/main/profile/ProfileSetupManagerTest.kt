package org.sopt.official.feature.main.profile

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Test
import org.sopt.official.localstorage.source.UserStorage
import org.sopt.official.model.UserStatus

class ProfileSetupManagerTest {
    private val userStorage: UserStorage = mockk(relaxed = true)
    private val manager = ProfileSetupManager(userStorage)

    @Test
    fun `비회원이면 플래그와 상관없이 등록 화면을 띄우지 않는다`() = runTest {
        every { userStorage.needProfileSetup } returns flowOf(true)

        assertFalse(manager.shouldOpenProfileSetup(UserStatus.UNAUTHENTICATED))
    }

    @Test
    fun `등록 완료 스킴을 받으면 플래그를 해제한다`() = runTest {
        manager.handleWebViewResult("sopt://profile/complete")

        coVerify(exactly = 1) { userStorage.saveNeedProfileSetup(false) }
    }

    @Test
    fun `웹뷰를 그냥 닫으면 플래그를 유지한다`() = runTest {
        manager.handleWebViewResult(null)

        coVerify(exactly = 0) { userStorage.saveNeedProfileSetup(any()) }
    }
}
