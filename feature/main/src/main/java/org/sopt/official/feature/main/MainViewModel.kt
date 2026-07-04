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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.home.model.AppService
import org.sopt.official.domain.home.usecase.GetTabAppServiceUseCase
import org.sopt.official.localstorage.source.UserStorage

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTabAppServiceUseCase: GetTabAppServiceUseCase,
    userStorage: UserStorage,
) : ViewModel() {

    private val isAppjamMode: StateFlow<Boolean> = userStorage.isAppjamMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _mainTabs = MutableStateFlow(MainTab.getActiveTabs(emptyList()))
    val mainTabs: StateFlow<List<MainTab>>
        get() = _mainTabs.asStateFlow()

    private val _badgeMap = MutableStateFlow<Map<MainTab, String?>>(emptyMap())
    val badgeMap: StateFlow<Map<MainTab, String?>>
        get() = _badgeMap.asStateFlow()

    init {
        observeAppServices()
    }

    fun updateBadge(badges: Map<String?, String?>) {
        _badgeMap.update {
            _mainTabs.value.associateWith { tab ->
                tab.deeplink?.let { badges[it] }
            }
        }
    }

    private fun observeAppServices() {
        viewModelScope.launch {
            getTabAppServiceUseCase()
                .onSuccess { appServices -> updateMainTabs(appServices) }
        }
    }

    private fun updateMainTabs(services: List<AppService>) {
        val badgeByDeeplink = services.associate {
            it.deepLink to if (it.displayAlarmBadge) it.alarmBadge else null
        }

        val deepLinks = services.map { it.deepLink }.filter { deepLink ->
            when (deepLink) {
                "soptamp" -> !isAppjamMode.value
                "appjamtamp" -> isAppjamMode.value
                else -> true
            }
        }

        _mainTabs.update { MainTab.getActiveTabs(deepLinks) }

        updateBadge(badgeByDeeplink)
    }
}
