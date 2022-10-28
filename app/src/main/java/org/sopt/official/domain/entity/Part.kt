package org.sopt.official.domain.entity

enum class Part(
    val title: String,
    val soptTitle: String,
) {
    ALl("all", "전체 공지"),
    PLAN("plan", "기획"),
    DESIGN("design", "디자인"),
    WEB("web", "Web"),
    ANDROID("android", "Android"),
    IOS("ios", "iOS"),
    SERVER("server", "Server");

    companion object {
        fun of(title: String): Part {
            return values().first { it.title == title }
        }
    }
}
