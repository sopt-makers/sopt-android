package org.sopt.official.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sopt.official.config.messaging.RemoteMessageType
import org.sopt.official.domain.entity.notification.NotificationHistoryItem
import org.sopt.official.domain.usecase.notification.GetNotificationHistoryUseCase
import org.sopt.official.domain.usecase.notification.UpdateEntireNotificationReadingStateUseCase
import org.sopt.official.domain.usecase.notification.UpdateNotificationReadingStateUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationDetailViewModel @Inject constructor(
    private val updateNotificationReadingStateUseCase: UpdateNotificationReadingStateUseCase,
) : ViewModel() {

    fun updateNotificationReadingState(id: Int) {
        viewModelScope.launch {
            updateNotificationReadingStateUseCase.invoke(id)
                .onFailure { Timber.e(it) }
        }
    }
}