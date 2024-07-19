/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.notification.NotificationHistoryItem
import org.sopt.official.domain.usecase.notification.GetNotificationHistoryUseCase
import org.sopt.official.domain.usecase.notification.UpdateEntireNotificationReadingStateUseCase
import timber.log.Timber

@HiltViewModel
class NotificationHistoryViewModel @Inject constructor(
    private val getNotificationHistoryUseCase: GetNotificationHistoryUseCase,
    private val updateEntireNotificationReadingStateUseCase: UpdateEntireNotificationReadingStateUseCase
) : ViewModel() {

    private val _notificationHistoryList = MutableStateFlow<List<NotificationHistoryItem>>(arrayListOf())
    val notificationHistoryList: StateFlow<List<NotificationHistoryItem>> get() = _notificationHistoryList.asStateFlow()

    private var currentPaginationIndex = 0
    private var notificationHistoryJob: Job? = null

    init {
        getNotificationHistory()
    }

    fun getNotificationHistory() {
        notificationHistoryJob?.let {
            if (it.isActive || !it.isCompleted) return
        }

        notificationHistoryJob = viewModelScope.launch {
            getNotificationHistoryUseCase.invoke(currentPaginationIndex)
                .onSuccess {
                    _notificationHistoryList.value = _notificationHistoryList.value.plus(it)
                    currentPaginationIndex++
                }
                .onFailure { Timber.e(it) }
        }
    }

    fun updateEntireNotificationReadingState() {
        viewModelScope.launch {
            updateEntireNotificationReadingStateUseCase.invoke()
        }
    }
}
