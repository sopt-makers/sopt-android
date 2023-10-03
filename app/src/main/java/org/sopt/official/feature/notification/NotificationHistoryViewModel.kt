package org.sopt.official.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.domain.entity.notification.NotificationHistoryItem
import org.sopt.official.domain.usecase.notification.GetNotificationHistoryUseCase
import org.sopt.official.domain.usecase.notification.UpdateEntireNotificationReadingStateUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationHistoryViewModel @Inject constructor(
    private val getNotificationHistoryUseCase: GetNotificationHistoryUseCase,
    private val updateEntireNotificationReadingStateUseCase: UpdateEntireNotificationReadingStateUseCase
) : ViewModel() {

    private val _notificationHistoryList = MutableStateFlow<List<NotificationHistoryItem>>(arrayListOf())
    val notificationHistoryList: StateFlow<List<NotificationHistoryItem>> get() = _notificationHistoryList.asStateFlow()

    init {
        getNotificationHistory(0)
    }

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
                .onSuccess {
                    val newNotificationList = _notificationHistoryList.value
                    for (notification in newNotificationList) {
                        notification.isRead = true
                    }
                    _notificationHistoryList.value = newNotificationList
                }
                .onFailure { Timber.e("updateEntireNotificationReadingStateUseCase: ", it) }
        }
    }
}