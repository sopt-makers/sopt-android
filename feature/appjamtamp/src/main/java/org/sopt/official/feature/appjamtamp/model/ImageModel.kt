package org.sopt.official.feature.appjamtamp.model

internal sealed interface ImageModel {
    data class Remote(val url: List<String>) : ImageModel {
        override fun isEmpty() = url.isEmpty()
        override val size = url.size
    }

    data class Local(val uri: List<String>) : ImageModel {
        override fun isEmpty() = uri.isEmpty()
        override val size = uri.size
    }

    data object Empty : ImageModel {
        override fun isEmpty() = true
        override val size = 1
    }

    fun isEmpty(): Boolean
    val size: Int
}
