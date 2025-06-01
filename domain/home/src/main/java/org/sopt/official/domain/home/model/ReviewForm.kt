package org.sopt.official.domain.home.model

data class ReviewForm(
    val title: String,
    val subTitle: String,
    val actionButtonName: String,
    val linkUrl: String,
    val isActive: Boolean
) {
    companion object {
        val default = ReviewForm(
            title = "",
            subTitle = "",
            actionButtonName = "",
            linkUrl = "",
            isActive = false
        )
    }
}
