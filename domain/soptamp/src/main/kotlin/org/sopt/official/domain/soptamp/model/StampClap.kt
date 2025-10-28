package org.sopt.official.domain.soptamp.model

data class StampClap(
    val clapCount: Int,
)

data class StampClapResult(
    val stampId: Int,
    val appliedCount: Int,
    val totalClapCount: Int,
)