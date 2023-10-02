package org.sopt.official.feature.home.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.sopt.official.R
import org.sopt.official.feature.web.WebUrlConstant

enum class HomeMenuType(
    @StringRes val title: Int,
    @StringRes val description: Int?,
    val url: String,
    @DrawableRes val icon: Int,
    val clickEventType: String
) {
    // 비회원
    SOPT_REVIEW_AUTHENTICATED_URL(
        R.string.main_unauthenticated_small_block_review,
        R.string.main_unauthenticated_small_block_review_description,
        WebUrlConstant.SOPT_REVIEW_URL,
        R.drawable.ic_review,
        "click_review"
    ),
    SOPT_PROJECT_AUTHENTICATED_URL(
        R.string.main_unauthenticated_small_block_project,
        R.string.main_unauthenticated_small_block_project_description,
        WebUrlConstant.SOPT_PROJECT_URL,
        R.drawable.ic_project,
        "click_project"
    ),
    SOPT_INSTAGRAM_AUTHENTICATED_URL(
        R.string.main_unauthenticated_small_block_instagram,
        R.string.main_unauthenticated_small_block_instagram_description,
        WebUrlConstant.SOPT_INSTAGRAM,
        R.drawable.ic_instagram,
        "click_instagram"
    ),
    SOPT_YOUTUBE_AUTHENTICATED_URL(
        R.string.main_unauthenticated_small_block_youtube,
        R.string.main_unauthenticated_small_block_youtube_description,
        WebUrlConstant.SOPT_OFFICIAL_YOUTUBE,
        R.drawable.ic_youtube,
        "click_youtube"
    ),
    SOPT_FAQ_AUTHENTICATED_URL(
        R.string.main_unauthenticated_small_block_faq,
        R.string.main_unauthenticated_small_block_faq_description,
        WebUrlConstant.SOPT_FAQ_URL,
        R.drawable.ic_faq,
        "click_faq"
    ),

    // 활동
    SOPT_CREW_ACTIVE_URL(
        R.string.main_active_small_block_crew,
        R.string.main_active_small_block_crew_description,
        WebUrlConstant.PLAYGROUNG_CREW_URL,
        R.drawable.ic_crew_white100,
        "click_group"
    ),
    SOPT_MEMBER_ACTIVE_URL(
        R.string.main_active_small_block_member,
        R.string.main_active_small_block_member_description,
        WebUrlConstant.PLAYGROUNG_MEMBER_URL,
        R.drawable.ic_member_white100,
        "click_member"
    ),
    SOPT_PROJECT_ACTIVE_URL(
        R.string.main_active_small_block_project,
        R.string.main_active_small_block_project_description,
        WebUrlConstant.PLAYGROUNG_PROJECT_URL,
        R.drawable.ic_project,
        "click_project"
    ),
    SOPT_OFFICIAL_PAGE_ACTIVE_URL(
        R.string.main_active_small_block_official_page,
        R.string.main_active_small_block_official_page_description,
        WebUrlConstant.SOPT_OFFICIAL_PAGE_URL,
        R.drawable.ic_homepage_white100,
        "click_homepage"
    ),

    // 비활동
    SOPT_MEMBER_INACTIVE_URL(
        R.string.main_inactive_small_block_member,
        R.string.main_inactive_small_block_member_description,
        WebUrlConstant.PLAYGROUNG_MEMBER_URL,
        R.drawable.ic_member_white100,
        "click_member"
    ),
    SOPT_PROJECT_INACTIVE_URL(
        R.string.main_inactive_small_block_project,
        R.string.main_inactive_small_block_project_description,
        WebUrlConstant.PLAYGROUNG_PROJECT_URL,
        R.drawable.ic_project,
        "click_project"
    ),
    SOPT_OFFICIAL_PAGE_INACTIVE_URL(
        R.string.main_inactive_small_block_official_page,
        null,
        WebUrlConstant.SOPT_OFFICIAL_PAGE_URL,
        R.drawable.ic_homepage_white100,
        "click_homepage"
    ),
    SOPT_INSTAGRAM_INACTIVE_URL(
        R.string.main_inactive_small_block_instagram,
        null,
        WebUrlConstant.SOPT_INSTAGRAM,
        R.drawable.ic_instagram,
        "click_instagram"
    ),
    SOPT_YOUTUBE_INACTIVE_URL(
        R.string.main_inactive_small_block_youtube,
        null,
        WebUrlConstant.SOPT_OFFICIAL_YOUTUBE,
        R.drawable.ic_youtube,
        "click_youtube"
    );
}
