package org.sopt.official.feature.home.navigation

enum class HomeUrl(val url: String) {
    POKE("home/poke"),
    FORTUNE("home/fortune"),
    SOPTAMP("home/soptamp"),
    UNKNOWN("");

    companion object {
        fun from(url: String?): HomeUrl {
            return entries.find { it.url == url } ?: UNKNOWN
        }
    }
}
