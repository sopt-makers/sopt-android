package org.sopt.official.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.official.data.model.notification.response.NotificationDetailResponse
import org.sopt.official.domain.usecase.notification.GetNotificationDetailUseCase
import org.sopt.official.domain.usecase.notification.UpdateNotificationReadingStateUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationDetailViewModel @Inject constructor(
    private val getNotificationDetailUseCase: GetNotificationDetailUseCase,
    private val updateNotificationReadingStateUseCase: UpdateNotificationReadingStateUseCase,
) : ViewModel() {

    private val _notificationDetail = MutableStateFlow<NotificationDetailResponse?>(null)
    val notificationDetail: StateFlow<NotificationDetailResponse?> get() = _notificationDetail

    fun getNotificationDetail(id: Long) {
        viewModelScope.launch {
            getNotificationDetailUseCase.invoke(id)
                .onSuccess {
                    _notificationDetail.value = it
                    updateNotificationReadingState(it.notificationId)
                }
                .onFailure { Timber.e(it) }
        }
    }

    private fun updateNotificationReadingState(id: Long) {
        viewModelScope.launch {
            updateNotificationReadingStateUseCase.invoke(id)
                .onFailure { Timber.e(it) }
        }
    }
}