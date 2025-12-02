package org.sopt.official.feature.poke.navigation

import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.MainTabRoute

sealed interface PokeRoute : MainTabRoute

@Serializable
data object PokeGraph : PokeRoute

@Serializable
data object PokeEntry : PokeRoute

@Serializable
data object PokeMain : PokeRoute

@Serializable
data class PokeOnboarding(
    val currentGeneration: Int = 0,
    val userStatus: String = ""
) : PokeRoute

@Serializable
data class PokeNotification(
    val userStatus: String = ""
) : PokeRoute

@Serializable
data class PokeFriendList(
    val initialFriendType: String? = null
) : PokeRoute