/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.home.model.HomeAppServiceInfo
import org.sopt.official.domain.home.usecase.GetTabAppServiceUseCase
import org.sopt.official.domain.home.usecase.ObserveTabAppServiceUseCase
import org.sopt.official.localstorage.source.UserStorage

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTabAppServiceUseCase: GetTabAppServiceUseCase,
    private val observeTabAppServiceUseCase: ObserveTabAppServiceUseCase,
    private val userStorage: UserStorage,
) : ViewModel() {

    private val _mainTabs = MutableStateFlow(MainTab.getActiveTabs(emptyList()))
    val mainTabs: StateFlow<List<MainTab>>
        get() = _mainTabs.asStateFlow()

    private val _badgeMap = MutableStateFlow<Map<MainTab, String?>>(emptyMap())
    val badgeMap: StateFlow<Map<MainTab, String?>>
        get() = _badgeMap.asStateFlow()

    init {
        fetchTabAppServices()
        // tab-app-service-info 응답이 isAppjamMode + 탭 활성화/뱃지의 단일 출처
        observeTabAppServiceUseCase()
            .filterNotNull()
            .onEach { info -> updateMainTabs(info) }
            .launchIn(viewModelScope)
    }

    private fun updateBadge(badges: Map<String?, String?>) {
        _badgeMap.update { current ->
            _mainTabs.value.associateWith { tab ->
                val deeplink = tab.deeplink
                if (deeplink != null && badges.containsKey(deeplink)) badges[deeplink] else current[tab]
            }
        }
    }

    private fun fetchTabAppServices() {
        viewModelScope.launch { getTabAppServiceUseCase() }
    }

    fun refreshTabAppServices() {
        viewModelScope.launch { getTabAppServiceUseCase(forceRefresh = true) }
    }

    private suspend fun updateMainTabs(info: HomeAppServiceInfo) {
        // MyPage 등 다른 화면은 UserStorage를 통해 isAppjamMode를 읽으므로,
        // 이 API 응답을 받는 이 지점이 유일한 저장 지점이어야 한다.
        userStorage.saveIsAppjamMode(info.isAppjamMode)

        val badgeByDeeplink = info.appServices
            .associate { it.deepLink to if (it.displayAlarmBadge) it.alarmBadge else null }

        val deepLinks = info.appServices.map { it.deepLink }.filter { deepLink ->
            when (deepLink) {
                "soptamp" -> !info.isAppjamMode
                "appjamtamp" -> info.isAppjamMode
                else -> true
            }
        }

        _mainTabs.update { MainTab.getActiveTabs(deepLinks) }
        updateBadge(badgeByDeeplink)
    }
}
