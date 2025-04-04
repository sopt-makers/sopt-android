package org.sopt.official.domain.auth.model

data class InformationWithCode(
    val name: String?,
    val phone: String,
    val code: String,
    val type: String
)
