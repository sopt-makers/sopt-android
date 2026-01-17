/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.notification.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.domain.notification.entity.Notification
import org.sopt.official.domain.notification.usecase.GetNotificationDetailUseCase
import org.sopt.official.domain.notification.usecase.UpdateNotificationReadingStateUseCase
import timber.log.Timber

@Inject
@ViewModelKey(NotificationDetailViewModel::class)
@ContributesIntoMap(AppScope::class)
class NotificationDetailViewModel @Inject constructor(
    private val getNotificationDetailUseCase: GetNotificationDetailUseCase,
    private val updateNotificationReadingStateUseCase: UpdateNotificationReadingStateUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val notificationId = savedStateHandle.get<String>("notificationId").orEmpty()
    private val _notificationDetail = MutableStateFlow<Notification?>(null)
    val notificationDetail: StateFlow<Notification?> = _notificationDetail.asStateFlow()

    init {
        getNotificationDetail(notificationId)
    }

    fun getNotificationDetail(id: String) {
        viewModelScope.launch {
            getNotificationDetailUseCase(id).onSuccess {
                _notificationDetail.value = it
                updateNotificationReadingState(it.notificationId)
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    private suspend fun updateNotificationReadingState(id: String) {
        runCatching {
            updateNotificationReadingStateUseCase(id)
        }.onFailure {
            Timber.e(it)
        }
    }
}