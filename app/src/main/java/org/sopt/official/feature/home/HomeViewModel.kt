package org.sopt.official.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Instant
import org.sopt.official.R
import org.sopt.official.domain.entity.UserActiveState
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.entity.home.HomeSection
import org.sopt.official.domain.entity.home.SoptActiveGeneration
import org.sopt.official.domain.entity.home.SoptActiveRecord
import org.sopt.official.domain.entity.home.SoptUser
import org.sopt.official.domain.entity.home.User
import org.sopt.official.domain.repository.home.HomeRepository
import org.sopt.official.domain.usecase.notification.RegisterPushTokenUseCase
import org.sopt.official.feature.home.model.HomeCTAType
import org.sopt.official.feature.home.model.HomeMenuType
import org.sopt.official.feature.home.model.SoptMainContentUrl
import org.sopt.official.feature.home.model.UserActiveGeneration
import org.sopt.official.feature.home.model.UserUiState
import org.sopt.official.util.calculateDurationOfGeneration
import org.sopt.official.util.calculateGenerationStartDate
import org.sopt.official.util.systemNow
import org.sopt.official.util.toDefaultLocalDate
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val registerPushTokenUseCase: RegisterPushTokenUseCase,
) : ViewModel() {

    private val homeUiState = MutableStateFlow(SoptUser())
    private val user = homeUiState.map { it.user }

    private val _description = MutableStateFlow(HomeSection())
    val description = _description.asStateFlow()
    val userActiveState = homeUiState
        .map { it.user.activeState }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UserActiveState.UNAUTHENTICATED)
    val isAllNotificationsConfirm: Flow<Boolean> = homeUiState.map { it.isAllConfirm }
    val generationList: Flow<SoptActiveGeneration?> = homeUiState
        .filterNotNull()
        .map { it.user.generationList }
    val title = user
        .map {
            val state = it.activeState
            val userName = it.name
            val generation = it.generationList?.last?.toInt() ?: 0
            val startDate = calculateGenerationStartDate(generation)
            val currentDate = Instant.systemNow().toDefaultLocalDate()
            val period = calculateDurationOfGeneration(startDate, currentDate)
            when {
                userName?.isNotEmpty() == true -> UserUiState.User(
                    R.string.main_title_member,
                    userName,
                    period.toString()
                )

                state == UserActiveState.INACTIVE -> UserUiState.InactiveUser(
                    R.string.main_title_inactive_member
                )

                else -> UserUiState.UnauthenticatedUser(R.string.main_title_non_member)
            }
        }
    val generatedTagText: Flow<UserActiveGeneration> = user
        .map {
            val state = it.activeState
            val generationList = it.generationList
            val isEmpty = generationList?.isEmpty
            val lastGeneration = if (isEmpty != true) generationList?.first.toString() else ""
            when {
                state == UserActiveState.ACTIVE -> UserActiveGeneration(
                    R.string.main_active_member,
                    lastGeneration
                )

                isEmpty == true && state == UserActiveState.INACTIVE -> UserActiveGeneration(
                    R.string.main_no_profile_member,
                    null
                )

                isEmpty != true && state == UserActiveState.INACTIVE -> UserActiveGeneration(
                    R.string.main_inactive_member,
                    lastGeneration
                )

                else -> UserActiveGeneration(R.string.main_non_member, null)
            }
        }
    val mainMenuContents: Flow<SoptMainContentUrl> = homeUiState
        .map {
            when (it.user.activeState) {
                UserActiveState.UNAUTHENTICATED -> SoptMainContentUrl(
                    HomeCTAType.SOPT_OFFICIAL_PAGE_URL,
                    HomeMenuType.SOPT_REVIEW_AUTHENTICATED_URL,
                    HomeMenuType.SOPT_PROJECT_AUTHENTICATED_URL
                )

                UserActiveState.ACTIVE -> SoptMainContentUrl(
                    HomeCTAType.SOPT_ATTENDANCE,
                    HomeMenuType.SOPT_CREW_ACTIVE_URL,
                    HomeMenuType.SOPT_MEMBER_ACTIVE_URL
                )

                else -> SoptMainContentUrl(
                    HomeCTAType.SOPT_CREW_URL,
                    HomeMenuType.SOPT_MEMBER_INACTIVE_URL,
                    HomeMenuType.SOPT_PROJECT_INACTIVE_URL
                )
            }
        }
    val subMenuContents = homeUiState
        .map {
            when (it.user.activeState) {
                UserActiveState.ACTIVE -> listOf(
                    HomeMenuType.SOPT_PROJECT_ACTIVE_URL,
                    HomeMenuType.SOPT_OFFICIAL_PAGE_ACTIVE_URL
                )

                UserActiveState.INACTIVE -> listOf(
                    HomeMenuType.SOPT_OFFICIAL_PAGE_INACTIVE_URL,
                    HomeMenuType.SOPT_INSTAGRAM_INACTIVE_URL,
                    HomeMenuType.SOPT_YOUTUBE_INACTIVE_URL
                )

                else -> listOf(
                    HomeMenuType.SOPT_INSTAGRAM_AUTHENTICATED_URL,
                    HomeMenuType.SOPT_YOUTUBE_AUTHENTICATED_URL,
                    HomeMenuType.SOPT_FAQ_AUTHENTICATED_URL
                )
            }
        }

    fun initHomeUi(userStatus: UserStatus) {
        viewModelScope.launch {
            if (userStatus != UserStatus.UNAUTHENTICATED) {
                homeRepository.getMainView()
                    .onSuccess {
                        homeUiState.value = it
                    }
                    .onFailure { error ->
                        if (error is HttpException && error.code() == 400) {
                            homeUiState.value = SoptUser(
                                User(activeState = UserActiveState.INACTIVE),
                                SoptActiveRecord(),
                                false
                            )
                        }
                        Timber.e(error)
                    }
            } else {
                homeUiState.value = SoptUser(
                    User(),
                    SoptActiveRecord()
                )
            }
        }
    }

    fun initMainDescription(userStatus: UserStatus) {
        viewModelScope.launch {
            if (userStatus != UserStatus.UNAUTHENTICATED) {
                homeRepository.getMainDescription()
                    .onSuccess {
                        _description.value = it
                    }
            }
        }
    }

    fun registerPushToken(userStatus: UserStatus) {
        if (userStatus == UserStatus.UNAUTHENTICATED) return
        viewModelScope.launch {
            runCatching {
                FirebaseMessaging.getInstance().token.await()
            }.onSuccess {
                registerPushTokenUseCase(it)
                    .onFailure(Timber::e)
            }.onFailure(Timber::e)
        }
    }
}
