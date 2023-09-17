package org.sopt.official.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.sopt.official.R
import org.sopt.official.base.BaseItemType
import org.sopt.official.domain.entity.UserState
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.entity.main.MainTitle
import org.sopt.official.domain.entity.main.MainUrl
import org.sopt.official.domain.entity.main.MainViewOperationInfo
import org.sopt.official.domain.entity.main.MainViewResult
import org.sopt.official.domain.entity.main.MainViewUserInfo
import org.sopt.official.domain.entity.main.UserActiveGeneration
import org.sopt.official.domain.repository.main.MainViewRepository
import org.sopt.official.feature.web.WebUrlConstant
import org.sopt.official.util.calculateDurationOfGeneration
import org.sopt.official.util.calculateGenerationStartDate
import org.sopt.official.util.systemNow
import org.sopt.official.util.toDefaultLocalDate
import org.sopt.official.util.wrapper.NullableWrapper
import org.sopt.official.util.wrapper.asNullableWrapper
import org.sopt.official.util.wrapper.getOrEmpty
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainViewRepository: MainViewRepository
) : ViewModel() {
    private var mainViewResult = MutableStateFlow(NullableWrapper.none<MainViewResult>())
    private val user = mainViewResult
        .map { it.get()?.user ?: MainViewUserInfo() }
    val title: Flow<MainTitle> = user
        .map {
            val state = it.status
            val userName = it.name.getOrEmpty()
            val generation = it.generationList.get()?.last()?.toInt() ?: 0
            val startDate = calculateGenerationStartDate(generation)
            val currentDate = Instant.systemNow().toDefaultLocalDate()
            val period = calculateDurationOfGeneration(startDate, currentDate)
            when {
                userName.isNotEmpty() -> MainTitle(R.string.main_title_member, userName, period.toString())
                state == UserState.INACTIVE -> MainTitle(R.string.main_title_inactive_member, null, null)
                else -> MainTitle(R.string.main_title_non_member, null, null)
            }
        }
    val generatedTagText: Flow<UserActiveGeneration> = user
        .map {
            val state = it.status
            val generationList = it.generationList.getOrEmpty()
            val isEmpty = generationList.isEmpty()
            val lastGeneration = if (!isEmpty) generationList[0].toString() else ""
            when {
                state == UserState.ACTIVE -> UserActiveGeneration(R.string.main_active_member, lastGeneration)
                isEmpty && state == UserState.INACTIVE -> UserActiveGeneration(R.string.main_no_profile_member, null)
                !isEmpty && state == UserState.INACTIVE -> UserActiveGeneration(R.string.main_inactive_member, lastGeneration)
                else -> UserActiveGeneration(R.string.main_non_member, null)
            }
        }
    val userState: Flow<NullableWrapper<UserState>> = mainViewResult
        .map { it.get()?.user?.status.asNullableWrapper() }
    val profileImage: Flow<NullableWrapper<String>> = mainViewResult
        .map { it.get()?.user?.profileImage?.get().asNullableWrapper() }
    val generationList: Flow<NullableWrapper<List<Long>>> = mainViewResult
        .map { it.get()?.user?.generationList?.get().asNullableWrapper() }
    val attendanceScore: Flow<NullableWrapper<Double>> = mainViewResult
        .map { it.get()?.operation?.attendanceScore?.get().asNullableWrapper() }
    val announcement: Flow<NullableWrapper<String>> = mainViewResult
        .map { it.get()?.operation?.announcement?.get().asNullableWrapper() }
    val blockItem: Flow<NullableWrapper<MainUrl>> = mainViewResult
        .map {
            val userState = it.get()?.user?.status
            when (userState) {
                // 비회원
                UserState.UNAUTHENTICATED -> MainUrl(
                    LargeBlockType.SOPT_OFFICIAL_PAGE_URL,
                    SmallBlockType.SOPT_REVIEW_AUTHENTICATED_URL,
                    SmallBlockType.SOPT_PROJECT_AUTHENTICATED_URL
                )

                UserState.ACTIVE -> MainUrl(
                    LargeBlockType.SOPT_ATTENDENCE,
                    SmallBlockType.SOPT_CREW_ACTIVE_URL,
                    SmallBlockType.SOPT_MEMBER_ACTIVE_URL
                )

                else -> MainUrl(
                    LargeBlockType.SOPT_CREW_URL,
                    SmallBlockType.SOPT_MEMBER_INACTIVE_URL,
                    SmallBlockType.SOPT_PROJECT_INACTIVE_URL
                )
            }.asNullableWrapper()
        }
    val blockList: Flow<List<SmallBlockItemHolder>> = mainViewResult
        .map {
            val userState = it.get()?.user?.status
            when (userState) {
                UserState.ACTIVE -> listOf(SmallBlockType.SOPT_PROJECT_ACTIVE_URL, SmallBlockType.SOPT_OFFICIAL_PAGE_ACTIVE_URL)
                UserState.INACTIVE -> listOf(SmallBlockType.SOPT_OFFICIAL_PAGE_INACTIVE_URL, SmallBlockType.SOPT_INSTAGRAM_INACTIVE_URL, SmallBlockType.SOPT_YOUTUBE_INACTIVE_URL)
                else -> listOf(SmallBlockType.SOPT_INSTAGRAM_AUTHENTICATED_URL, SmallBlockType.SOPT_YOUTUBE_AUTHENTICATED_URL, SmallBlockType.SOPT_FAQ_AUTHENTICATED_URL)
            }
        }
        .map { list -> list.map { SmallBlockItemHolder.SmallBlock(it) } }

    fun initMainView(userStatus: UserStatus) {
        viewModelScope.launch {
            if (userStatus != UserStatus.UNAUTHENTICATED) {
                mainViewRepository.getMainView()
                    .onSuccess {
                        mainViewResult.value = it.asNullableWrapper()
                    }.onFailure { error ->
                        if (error is HttpException) {
                            if (error.code() == 400) {
                                mainViewResult.value = MainViewResult(
                                    MainViewUserInfo(status = UserState.INACTIVE),
                                    MainViewOperationInfo()
                                ).asNullableWrapper()
                            }
                        }
                        Timber.e(error)
                    }
            } else {
                mainViewResult.value = MainViewResult(
                    MainViewUserInfo(),
                    MainViewOperationInfo()
                ).asNullableWrapper()
            }
        }
    }

    sealed class SmallBlockItemHolder : BaseItemType {
        data class SmallBlock(val item: SmallBlockType) : SmallBlockItemHolder()
    }

    enum class LargeBlockType(
        val title: Int,
        val description: Int?,
        val url: String?,
        val icon: Int
    ) {
        SOPT_OFFICIAL_PAGE_URL(
            R.string.main_unauthenticated_large_block_official_page,
            null,
            WebUrlConstant.SOPT_OFFICIAL_PAGE_URL,
            R.drawable.ic_homepage_orange
        ),
        SOPT_ATTENDENCE(
            R.string.main_active_large_block_attendance,
            R.string.main_active_large_block_attendance_description,
            null,
            R.drawable.ic_attendance
        ),
        SOPT_CREW_URL(
            R.string.main_inactive_large_block_crew,
            R.string.main_inactive_large_block_crew_description,
            WebUrlConstant.PLAYGROUNG_CREW_URL,
            R.drawable.ic_crew_orange
        ),
    }

    enum class SmallBlockType(
        val title: Int,
        val description: Int?,
        val url: String,
        val icon: Int
    ) {
        // 비회원
        SOPT_REVIEW_AUTHENTICATED_URL(
            R.string.main_unauthenticated_small_block_review,
            R.string.main_unauthenticated_small_block_review_description,
            WebUrlConstant.SOPT_REVIEW_URL,
            R.drawable.ic_review
        ),
        SOPT_PROJECT_AUTHENTICATED_URL(
            R.string.main_unauthenticated_small_block_project,
            R.string.main_unauthenticated_small_block_project_description,
            WebUrlConstant.SOPT_PROJECT_URL,
            R.drawable.ic_project
        ),
        SOPT_INSTAGRAM_AUTHENTICATED_URL(
            R.string.main_unauthenticated_small_block_instagram,
            R.string.main_unauthenticated_small_block_instagram_description,
            WebUrlConstant.SOPT_INSTAGRAM,
            R.drawable.ic_instagram
        ),
        SOPT_YOUTUBE_AUTHENTICATED_URL(
            R.string.main_unauthenticated_small_block_youtube,
            R.string.main_unauthenticated_small_block_youtube_description,
            WebUrlConstant.SOPT_OFFICIAL_YOUTUBE,
            R.drawable.ic_youtube
        ),
        SOPT_FAQ_AUTHENTICATED_URL(
            R.string.main_unauthenticated_small_block_faq,
            R.string.main_unauthenticated_small_block_faq_description,
            WebUrlConstant.SOPT_FAQ_URL,
            R.drawable.ic_faq
        ),

        //활동
        SOPT_CREW_ACTIVE_URL(
            R.string.main_active_small_block_crew,
            R.string.main_active_small_block_crew_description,
            WebUrlConstant.PLAYGROUNG_CREW_URL,
            R.drawable.ic_crew_white100
        ),
        SOPT_MEMBER_ACTIVE_URL(
            R.string.main_active_small_block_member,
            R.string.main_active_small_block_member_description,
            WebUrlConstant.PLAYGROUNG_MEMBER_URL,
            R.drawable.ic_member_white100
        ),
        SOPT_PROJECT_ACTIVE_URL(
            R.string.main_active_small_block_project,
            R.string.main_active_small_block_project_description,
            WebUrlConstant.PLAYGROUNG_PROJECT_URL,
            R.drawable.ic_project
        ),
        SOPT_OFFICIAL_PAGE_ACTIVE_URL(
            R.string.main_active_small_block_official_page,
            R.string.main_active_small_block_official_page_description,
            WebUrlConstant.SOPT_OFFICIAL_PAGE_URL,
            R.drawable.ic_homepage_white100
        ),

        // 비활동
        SOPT_MEMBER_INACTIVE_URL(
            R.string.main_inactive_small_block_member,
            R.string.main_inactive_small_block_member_description,
            WebUrlConstant.PLAYGROUNG_MEMBER_URL,
            R.drawable.ic_member_white100
        ),
        SOPT_PROJECT_INACTIVE_URL(
            R.string.main_inactive_small_block_project,
            R.string.main_inactive_small_block_project_description,
            WebUrlConstant.PLAYGROUNG_PROJECT_URL,
            R.drawable.ic_project
        ),
        SOPT_OFFICIAL_PAGE_INACTIVE_URL(
            R.string.main_inactive_small_block_official_page,
            null,
            WebUrlConstant.SOPT_OFFICIAL_PAGE_URL,
            R.drawable.ic_homepage_white100
        ),
        SOPT_INSTAGRAM_INACTIVE_URL(
            R.string.main_inactive_small_block_instagram,
            null,
            WebUrlConstant.SOPT_INSTAGRAM,
            R.drawable.ic_instagram
        ),
        SOPT_YOUTUBE_INACTIVE_URL(
            R.string.main_inactive_small_block_youtube,
            null,
            WebUrlConstant.SOPT_OFFICIAL_YOUTUBE,
            R.drawable.ic_youtube
        );
    }

    companion object {
        private const val EVEN_DATE = ""
        private const val ODD_DATE = ""
    }
}