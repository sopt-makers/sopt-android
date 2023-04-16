package org.sopt.official.feature.web

object WebUrlConstant {
    //region sopt

    val SOPT_OFFICIAL_PAGE_URL = toSoptUrl("")
    val SOPT_REVIEW_URL = toSoptUrl("review")
    val SOPT_PROJECT_URL = toSoptUrl("project")
    val SOPT_FAQ_URL = toSoptUrl("FAQ")

    //endregion

    //region playground

    val PLAYGROUNG_MEMBER_URL = toPlaygroundUrl("members")
    val PLAYGROUNG_PROJECT_URL = toPlaygroundUrl("projects")
    val PLAYGROUNG_CREW_URL = toPlaygroundUrl("group")

    //endregion

    //region others

    val SOPT_OFFICIAL_YOUTUBE = "https://www.youtube.com/@SOPTMEDIA"

    //endregion

    //region base

    val SOPT_BASE_URL = "https://sopt.arg"
    val PLAYGROUNG_BASE_URL = "https://playground.sopt.org"

    fun toSoptUrl(url: String): String =
        if(url.isNotEmpty()) "${SOPT_BASE_URL}/${url}"
        else SOPT_BASE_URL

    fun toPlaygroundUrl(url: String): String =
        if(url.isNotEmpty()) "${PLAYGROUNG_BASE_URL}/${url}"
        else PLAYGROUNG_BASE_URL

    //endregion
}