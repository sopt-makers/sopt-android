package org.sopt.official.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.data.model.notification.response.NotificationHistoryItemResponse
import org.sopt.official.domain.usecase.notification.GetNotificationHistoryUseCase
import org.sopt.official.domain.usecase.notification.UpdateEntireNotificationReadingStateUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationHistoryViewModel @Inject constructor(
    private val getNotificationHistoryUseCase: GetNotificationHistoryUseCase,
    private val updateEntireNotificationReadingStateUseCase: UpdateEntireNotificationReadingStateUseCase
) : ViewModel() {

    private val _notificationHistoryList = MutableStateFlow<ArrayList<NotificationHistoryItemResponse>>(arrayListOf())
    val notificationHistoryList = _notificationHistoryList.asStateFlow()

    private val _updateEntireNotificationReadingState = MutableStateFlow(false)
    val updateEntireNotificationReadingState = _updateEntireNotificationReadingState.asStateFlow()

    fun getNotificationHistory(page: Int) {
        viewModelScope.launch {
            getNotificationHistoryUseCase.invoke(page)
                .onSuccess { _notificationHistoryList.value = it }
                .onFailure { Timber.e(it) }
        }
    }

    fun updateEntireNotificationReadingState() {
        viewModelScope.launch {
            updateEntireNotificationReadingStateUseCase.invoke()
                .onSuccess { Timber.d("updateEntireNotificationReadingStateUseCase: ", it) }
                .onFailure { Timber.e("updateEntireNotificationReadingStateUseCase: ", it) }
        }
    }
}