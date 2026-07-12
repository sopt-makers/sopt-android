package org.sopt.official.feature.home.model

enum class HomeOfficialChannel(
    val title: String,
    val link: String
) {
    HOMEPAGE(
        title = "홈페이지",
        link = "https://www.sopt.org/"
    ),
    INSTAGRAM(
        title = "인스타",
        link = "https://www.instagram.com/sopt_official/"
    ),
    YOUTUBE(
        title = "유튜브",
        link = "https://www.youtube.com/@SOPTMEDIA"
    )
}
