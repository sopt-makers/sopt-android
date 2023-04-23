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
    val NOTICE_PRIVATE_INFO = "https://florentine-legend-ffc.notion.site/SOPT-0b378275554d4d65a442310e83c7c988"
    val NOTICE_SERVICE_RULE = "https://florentine-legend-ffc.notion.site/SOPT-69e33dccb59543dd91f5f44ed1250881"

    //endregion

    //region base

    private const val SOPT_BASE_URL = "https://sopt.org"
    private const val PLAYGROUNG_BASE_URL = "https://playground.sopt.org"

    fun toSoptUrl(url: String): String =
        if (url.isNotEmpty()) "${SOPT_BASE_URL}/${url}"
        else SOPT_BASE_URL

    fun toPlaygroundUrl(url: String): String =
        if (url.isNotEmpty()) "${PLAYGROUNG_BASE_URL}/${url}"
        else PLAYGROUNG_BASE_URL

    //endregion
}