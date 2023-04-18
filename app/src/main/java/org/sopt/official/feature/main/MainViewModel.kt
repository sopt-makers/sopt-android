package org.sopt.official.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.sopt.official.R
import org.sopt.official.base.BaseItemType
import org.sopt.official.domain.entity.UserState
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.domain.entity.main.MainViewOperationInfo
import org.sopt.official.domain.entity.main.MainViewResult
import org.sopt.official.domain.entity.main.MainViewUserInfo
import org.sopt.official.domain.repository.main.MainViewRepository
import org.sopt.official.feature.web.WebUrlConstant
import org.sopt.official.util.computeMothUntilNow
import org.sopt.official.util.coroutine.stateInLazily
import org.sopt.official.util.wrapper.NullableWrapper
import org.sopt.official.util.wrapper.asNullableWrapper
import org.sopt.official.util.wrapper.getOrEmpty
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainViewRepository: MainViewRepository,
) : ViewModel() {
    private var mainViewResult = MutableStateFlow(NullableWrapper.none<MainViewResult>())
    val loginStatus = MutableStateFlow(NullableWrapper.none<UserStatus>())
    val title: StateFlow<Triple<Int, String?, String?>>
        get() = mainViewResult
            .map { it.get()?.user ?: MainViewUserInfo() }
            .map {
                val state = it.status
                val userName = it.name.getOrEmpty()
                val period = computeMothUntilNow(it.generationList.get()?.last()?.toInt() ?: 0)
                when {
                    userName.isNotEmpty() -> Triple(R.string.main_title_member, userName, period.toString())
                    state == UserState.INACTIVE -> Triple(R.string.main_title_inactive_member, null, null)
                    else -> Triple(R.string.main_title_non_member, null, null)
                }
            }
            .stateInLazily(viewModelScope, Triple(R.string.main_title_non_member, null, null))
    val generatedTagText: StateFlow<Pair<Int, String?>>
        get() = mainViewResult
            .map { it.get()?.user ?: MainViewUserInfo() }
            .map {
                val state = it.status
                val generationList = it.generationList.getOrEmpty()
                val isEmpty = generationList.isEmpty()
                val lastGeneration = if (!isEmpty) generationList[0].toString() else ""
                when {
                    state == UserState.ACTIVE -> Pair(R.string.main_active_member, lastGeneration)
                    isEmpty && state == UserState.INACTIVE -> Pair(R.string.main_no_profile_member, null)
                    !isEmpty && state == UserState.INACTIVE -> Pair(R.string.main_inactive_member, lastGeneration)
                    else -> Pair(R.string.main_non_member, null)
                }
            }
            .stateInLazily(viewModelScope, Pair(R.string.main_title_non_member, null))
    val userState: StateFlow<NullableWrapper<UserState>>
        get() = mainViewResult
            .map { it.get()?.user?.status.asNullableWrapper() }
            .stateInLazily(viewModelScope)
    val profileImage: StateFlow<NullableWrapper<String>>
        get() = mainViewResult
            .map { it.get()?.user?.profileImage?.get().asNullableWrapper() }
            .stateInLazily(viewModelScope)
    val generationList: StateFlow<NullableWrapper<List<Long>>>
        get() = mainViewResult
            .map { it.get()?.user?.generationList?.get().asNullableWrapper() }
            .stateInLazily(viewModelScope)
    val attendanceScore: StateFlow<NullableWrapper<Double>>
        get() = mainViewResult
            .map { it.get()?.operation?.attendanceScore?.get().asNullableWrapper() }
            .stateInLazily(viewModelScope)
    val announcement: StateFlow<NullableWrapper<String>>
        get() = mainViewResult
            .map { it.get()?.operation?.announcement?.get().asNullableWrapper() }
            .stateInLazily(viewModelScope)
    val blockItem: StateFlow<NullableWrapper<Triple<LargeBlockType, SmallBlockType, SmallBlockType>>>
        get() = mainViewResult
            .map {
                val userState = it.get()?.user?.status
                when (userState) {
                    UserState.ACTIVE -> Triple(
                        LargeBlockType.SOPT_OFFICIAL_PAGE_URL,
                        SmallBlockType.SOPT_REVIEW_URL,
                        SmallBlockType.SOPT_PROJECT_URL
                    )
                    UserState.INACTIVE -> Triple(
                        LargeBlockType.SOPT_ATTENDENCE,
                        SmallBlockType.PLAYGROUNG_MEMBER_URL,
                        SmallBlockType.PLAYGROUNG_PROJECT_URL
                    )
                    else -> Triple(
                        LargeBlockType.SOPT_FAQ_URL,
                        SmallBlockType.PLAYGROUNG_MEMBER_URL,
                        SmallBlockType.PLAYGROUNG_PROJECT_URL
                    )
                }.asNullableWrapper()
            }
            .stateInLazily(viewModelScope)
    val blockList: StateFlow<List<SmallBlockItemHolder>>
        get() = mainViewResult
            .map {
                val userState = it.get()?.user?.status
                when (userState) {
                    UserState.ACTIVE -> listOf(SmallBlockType.SOPT_FAQ_URL, SmallBlockType.SOPT_OFFICIAL_YOUTUBE)
                    UserState.INACTIVE -> listOf(SmallBlockType.SOPT_OFFICIAL_PAGE_URL, SmallBlockType.PLAYGROUNG_CREW_URL)
                    else -> listOf(SmallBlockType.PLAYGROUNG_CREW_URL, SmallBlockType.SOPT_OFFICIAL_PAGE_URL)
                }
            }
            .map { list -> list.map { SmallBlockItemHolder.SmallBlock(it) } }
            .stateInLazily(viewModelScope, emptyList())

    init {
        getMainView()
    }

    fun getMainView() {
        viewModelScope.launch {
            launch {
                loginStatus.collect {
                    if (it.getOrElse(UserStatus.UNAUTHENTICATED) == UserStatus.UNAUTHENTICATED) {
                        mainViewRepository.getMainView()
                            .onSuccess {
                                mainViewResult.value = it.asNullableWrapper()
                            }.onFailure {
                                if (it is HttpException) {
                                    if (it.code() == 400) {
                                        mainViewResult.value = MainViewResult(
                                            MainViewUserInfo(status = UserState.INACTIVE),
                                            MainViewOperationInfo()
                                        ).asNullableWrapper()
                                    }
                                }
                                Timber.e(it)
                            }
                    } else {
                        mainViewResult.value = MainViewResult(
                            MainViewUserInfo(),
                            MainViewOperationInfo()
                        ).asNullableWrapper()
                    }
                }
            }
        }
    }

    sealed class SmallBlockItemHolder : BaseItemType {
        data class SmallBlock(
            val item: SmallBlockType,
        ) : SmallBlockItemHolder()
    }

/*
sealed class ContentItemHolder : BaseItemType {
    data class Content(
        val icon: Int?,
        val name: String,
    ) : ContentItemHolder()
}

*/

    enum class LargeBlockType(val title: Int, val description: Int?, val url: String?, val icon: Int) {
        SOPT_OFFICIAL_PAGE_URL(
            R.string.main_large_block_official_page,
            null,
            WebUrlConstant.SOPT_OFFICIAL_PAGE_URL,
            R.drawable.ic_homepage
        ),
        SOPT_ATTENDENCE(
            R.string.main_large_block_attendance_title,
            R.string.main_large_block_attendance_description,
            null,
            R.drawable.ic_review
        ),
        SOPT_FAQ_URL(
            R.string.main_large_block_faq_title,
            R.string.main_large_block_faq_description,
            WebUrlConstant.SOPT_FAQ_URL,
            R.drawable.ic_faq
        ),
    }

    enum class SmallBlockType(val title: Int, val url: String, val icon: Int) {
        // sopt
        SOPT_OFFICIAL_PAGE_URL(R.string.main_small_block_official_page, WebUrlConstant.SOPT_OFFICIAL_PAGE_URL, R.drawable.ic_homepage),
        SOPT_REVIEW_URL(R.string.main_small_block_review, WebUrlConstant.SOPT_REVIEW_URL, R.drawable.ic_review),
        SOPT_PROJECT_URL(R.string.main_large_block_official_page, WebUrlConstant.SOPT_PROJECT_URL, R.drawable.ic_project),
        SOPT_FAQ_URL(R.string.main_small_block_faq, WebUrlConstant.SOPT_FAQ_URL, R.drawable.ic_faq),

        //playground
        PLAYGROUNG_MEMBER_URL(R.string.main_small_block_member, WebUrlConstant.PLAYGROUNG_MEMBER_URL, R.drawable.ic_member),
        PLAYGROUNG_PROJECT_URL(R.string.main_small_block_playground_project, WebUrlConstant.PLAYGROUNG_PROJECT_URL, R.drawable.ic_project),
        PLAYGROUNG_CREW_URL(R.string.main_small_block_crew, WebUrlConstant.PLAYGROUNG_CREW_URL, R.drawable.ic_crew),

        //others
        SOPT_OFFICIAL_YOUTUBE(R.string.main_small_block_youtube, WebUrlConstant.SOPT_OFFICIAL_YOUTUBE, R.drawable.ic_youtube),
    }

    companion object {
        private const val EVEN_DATE = ""
        private const val ODD_DATE = ""
    }
}