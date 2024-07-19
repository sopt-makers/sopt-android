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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.data.model.notification.response.NotificationDetailResponse
import org.sopt.official.domain.usecase.notification.GetNotificationDetailUseCase
import org.sopt.official.domain.usecase.notification.UpdateNotificationReadingStateUseCase
import timber.log.Timber

@HiltViewModel
class NotificationDetailViewModel @Inject constructor(
    private val getNotificationDetailUseCase: GetNotificationDetailUseCase,
    private val updateNotificationReadingStateUseCase: UpdateNotificationReadingStateUseCase,
) : ViewModel() {

    private val _notificationDetail = MutableStateFlow<NotificationDetailResponse?>(null)
    val notificationDetail: StateFlow<NotificationDetailResponse?> get() = _notificationDetail

    fun getNotificationDetail(id: String) {
        viewModelScope.launch {
            getNotificationDetailUseCase.invoke(id)
                .onSuccess {
                    _notificationDetail.value = it
                    updateNotificationReadingState(it.notificationId)
                }
                .onFailure { Timber.e(it) }
        }
    }

    private fun updateNotificationReadingState(id: String) {
        viewModelScope.launch {
            updateNotificationReadingStateUseCase.invoke(id)
        }
    }
}
