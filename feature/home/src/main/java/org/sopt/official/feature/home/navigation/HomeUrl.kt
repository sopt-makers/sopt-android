package org.sopt.official.feature.home.navigation

enum class HomeUrl(val url: String) {
    POKE("home/poke"),
    FORTUNE("home/fortune"),
    SOPTAMP("home/soptamp");

    companion object {
        fun from(url: String?): HomeUrl? {
            if (url == null) return null
            return entries.find { it.url == url }
        }
    }
}
