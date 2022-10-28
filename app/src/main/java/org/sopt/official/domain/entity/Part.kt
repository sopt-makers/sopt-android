package org.sopt.official.domain.entity

enum class Part(
    val title: String
) {
    PLAN("plan"),
    DESIGN("design"),
    WEB("web"),
    ANDROID("android"),
    IOS("ios"),
    SERVER("server");

    companion object {
        fun of(title: String): Part {
            return values().first { it.title == title }
        }
    }
}
