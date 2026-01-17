/*
 * MIT License
 * Copyright 2022-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.swiftzer.semver.SemVer
import org.sopt.official.auth.model.CentralizeToken
import org.sopt.official.auth.repository.CentralizeAuthRepository
import org.sopt.official.common.config.remoteconfig.SoptRemoteConfig
import org.sopt.official.common.config.remoteconfig.UpdateConfigModel
import org.sopt.official.common.di.AppScope
import org.sopt.official.model.UserStatus
import org.sopt.official.network.persistence.SoptDataStore
import timber.log.Timber

private const val DEFAULT_VERSION = "9.9.9"

sealed interface AuthUiEvent {
    data class Success(val userStatus: UserStatus) : AuthUiEvent
    data class Failure(val message: String) : AuthUiEvent
}

sealed interface UpdateState {
    data object Default : UpdateState
    data object NoUpdateAvailable : UpdateState
    data class PatchUpdateAvailable(val message: String) : UpdateState
    data class UpdateRequired(val message: String) : UpdateState
}

@Inject
@ViewModelKey(AuthViewModel::class)
@ContributesIntoMap(AppScope::class)
class AuthViewModel @Inject constructor(
    private val remoteConfig: SoptRemoteConfig,
    private val authRepository: CentralizeAuthRepository,
    private val dataStore: SoptDataStore
) : ViewModel() {
    // TODO: 삭제 예정
    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Default)
    val updateState get() = _updateState.asStateFlow()

    init {
        if (dataStore.accessToken.isNotEmpty() && dataStore.refreshToken.isNotEmpty()) {
            viewModelScope.launch {
                authRepository.refreshToken(
                    CentralizeToken(
                        accessToken = dataStore.accessToken,
                        refreshToken = dataStore.refreshToken
                    )
                ).onFailure {
                    Timber.e(it)
                    dataStore.clear()
                }
            }
        }
    }

    fun getUpdateConfig(version: String?) {
        viewModelScope.launch {
            remoteConfig.getVersionConfig().onSuccess { config ->
                val updateState = checkForUpdate(version, config)
                _updateState.update { updateState }
            }.onFailure {
                Timber.e(it)
                _updateState.update { UpdateState.NoUpdateAvailable }
            }
        }
    }

    private fun checkForUpdate(appVersion: String?, versionConfig: UpdateConfigModel): UpdateState {
        val currentVersion = parseToSemVer(appVersion)
        val latestAppVersion = parseToSemVer(versionConfig.latestVersion)

        return when {
            currentVersion.major < latestAppVersion.major -> UpdateState.UpdateRequired(versionConfig.forcedUpdateNotice)

            currentVersion.major == latestAppVersion.major && currentVersion.minor < latestAppVersion.minor ->
                UpdateState.UpdateRequired(versionConfig.forcedUpdateNotice)

            currentVersion.major == latestAppVersion.major &&
                currentVersion.minor == latestAppVersion.minor &&
                currentVersion.patch < latestAppVersion.patch ->
                UpdateState.PatchUpdateAvailable(versionConfig.optionalUpdateNotice)

            else -> UpdateState.NoUpdateAvailable
        }
    }

    private fun parseToSemVer(version: String?): SemVer {
        return try {
            if (version == null) return SemVer.parse(DEFAULT_VERSION)
            SemVer.parse(version)
        } catch (e: Exception) {
            Timber.e(e)
            SemVer.parse(DEFAULT_VERSION)
        }
    }
}