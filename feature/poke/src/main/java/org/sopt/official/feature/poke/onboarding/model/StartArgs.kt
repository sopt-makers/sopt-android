package org.sopt.official.feature.poke.onboarding.model

import java.io.Serializable

data class StartArgs(
    val currentGeneration: Int,
    val userStatus: String,
) : Serializable