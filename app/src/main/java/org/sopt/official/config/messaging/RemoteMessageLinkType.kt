package org.sopt.official.config.messaging

enum class RemoteMessageLinkType {
    WEB_LINK, DEEP_LINK;

    companion object {
        fun of(name: String) = entries.find {
            it.name == name
        }
    }
}