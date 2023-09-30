package org.sopt.official.config.messaging

enum class RemoteMessageType {
    NOTICE, NEWS;

    companion object {
        fun of(name: String) = entries.find {
            it.name == name
        }
    }
}