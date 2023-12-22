package org.sopt.official.domain.poke.entity

class CheckNewInPokeResponse : BaseResponse<CheckNewInPoke>()

data class CheckNewInPoke(
    val isNew: Boolean,
)