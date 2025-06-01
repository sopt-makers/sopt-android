package org.sopt.official.domain.home.model

data class FloatingToast(
    val imageUrl: String,
    val title: String,
    val expandedSubTitle: String,
    val collapsedSubtitle: String,
    val actionButtonName: String,
    val linkUrl: String,
    val active: Boolean
) {
    companion object {
        val default = FloatingToast(
            imageUrl = "",
            title = "",
            expandedSubTitle = "",
            collapsedSubtitle = "",
            actionButtonName = "",
            linkUrl = "",
            active = false
        )
    }
}
