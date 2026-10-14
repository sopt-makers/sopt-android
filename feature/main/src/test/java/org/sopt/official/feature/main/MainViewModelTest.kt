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
package org.sopt.official.feature.main

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.sopt.official.domain.home.model.AppService
import org.sopt.official.domain.home.model.HomeAppServiceInfo
import org.sopt.official.domain.home.repository.HomeRepository
import org.sopt.official.domain.home.usecase.GetTabAppServiceUseCase
import org.sopt.official.domain.home.usecase.ObserveTabAppServiceUseCase
import org.sopt.official.localstorage.source.UserStorage

/**
 * home/app-service의 badgeContentList가 MainViewModel의 뱃지 맵을 덮어쓰던 문제를 고치면서,
 * tab-app-service-info 하나가 활성 탭/뱃지/isAppjamMode의 단일 출처가 되도록 정리했다.
 * 이 테스트는 그 소유권 구조가 계속 유지되는지를 고정한다.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val homeRepository: HomeRepository = mockk()
    private val userStorage: UserStorage = mockk(relaxed = true)
    private val tabAppServiceFlow = MutableStateFlow<HomeAppServiceInfo?>(null)

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { homeRepository.observeTabAppService() } returns tabAppServiceFlow
        coEvery { homeRepository.getTabAppService(any()) } answers {
            Result.success(tabAppServiceFlow.value ?: HomeAppServiceInfo(isAppjamMode = false, appServices = emptyList()))
        }

        viewModel = MainViewModel(
            getTabAppServiceUseCase = GetTabAppServiceUseCase(homeRepository),
            observeTabAppServiceUseCase = ObserveTabAppServiceUseCase(homeRepository),
            userStorage = userStorage,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun appService(deepLink: String, displayBadge: Boolean, badge: String = "") = AppService(
        serviceName = deepLink,
        displayAlarmBadge = displayBadge,
        alarmBadge = badge,
        iconUrl = null,
        deepLink = deepLink,
    )

    @Test
    fun `tab-app-service-info 응답으로 활성 탭과 뱃지를 구성한다`() {
        tabAppServiceFlow.value = HomeAppServiceInfo(
            isAppjamMode = false,
            appServices = listOf(
                appService("poke", displayBadge = true, badge = "3"),
                appService("soptamp", displayBadge = false),
            ),
        )

        assertEquals(
            listOf(MainTab.Home, MainTab.Soptamp, MainTab.Poke, MainTab.MyPage),
            viewModel.mainTabs.value
        )
        assertEquals("3", viewModel.badgeMap.value[MainTab.Poke])
        assertNull(viewModel.badgeMap.value[MainTab.Soptamp])
    }

    @Test
    fun `isAppjamMode가 true면 soptamp 대신 appjamtamp 탭이 활성화된다`() {
        tabAppServiceFlow.value = HomeAppServiceInfo(
            isAppjamMode = true,
            appServices = listOf(
                appService("soptamp", displayBadge = false),
                appService("appjamtamp", displayBadge = false),
            ),
        )

        assertTrue(viewModel.mainTabs.value.contains(MainTab.Appjamtamp))
        assertFalse(viewModel.mainTabs.value.contains(MainTab.Soptamp))
    }

    @Test
    fun `isAppjamMode가 false면 appjamtamp 대신 soptamp 탭이 활성화된다`() {
        tabAppServiceFlow.value = HomeAppServiceInfo(
            isAppjamMode = false,
            appServices = listOf(
                appService("soptamp", displayBadge = false),
                appService("appjamtamp", displayBadge = false),
            ),
        )

        assertTrue(viewModel.mainTabs.value.contains(MainTab.Soptamp))
        assertFalse(viewModel.mainTabs.value.contains(MainTab.Appjamtamp))
    }

    @Test
    fun `응답의 isAppjamMode를 UserStorage에 저장한다 (단일 owner)`() {
        tabAppServiceFlow.value = HomeAppServiceInfo(isAppjamMode = true, appServices = emptyList())

        coVerify { userStorage.saveIsAppjamMode(true) }
    }

    @Test
    fun `같은 탭이 계속 활성 상태여도 displayAlarmBadge가 false로 바뀌면 뱃지가 사라진다`() {
        tabAppServiceFlow.value = HomeAppServiceInfo(
            isAppjamMode = false,
            appServices = listOf(appService("poke", displayBadge = true, badge = "5")),
        )
        assertEquals("5", viewModel.badgeMap.value[MainTab.Poke])

        tabAppServiceFlow.value = HomeAppServiceInfo(
            isAppjamMode = false,
            appServices = listOf(appService("poke", displayBadge = false)),
        )
        assertNull(viewModel.badgeMap.value[MainTab.Poke])
    }

    @Test
    fun `tab-app-service-info에는 home 딥링크가 오지 않으므로 Home 탭 뱃지는 항상 null이다`() {
        tabAppServiceFlow.value = HomeAppServiceInfo(
            isAppjamMode = false,
            appServices = listOf(
                appService("poke", displayBadge = true, badge = "1"),
                appService("soptamp", displayBadge = true, badge = "1"),
            ),
        )

        assertTrue(viewModel.mainTabs.value.contains(MainTab.Home))
        assertNull(viewModel.badgeMap.value[MainTab.Home])
    }

    @Test
    fun `refreshTabAppServices는 forceRefresh=true로 다시 요청한다`() {
        viewModel.refreshTabAppServices()

        coVerify { homeRepository.getTabAppService(true) }
    }
}
