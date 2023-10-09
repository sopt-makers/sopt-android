package org.sopt.official.domain.entity.home

import org.sopt.official.domain.entity.UserActiveState

data class SoptUser(
    val user: User = User(),
    val operation: SoptActiveRecord = SoptActiveRecord(),
    val isAllConfirm: Boolean = false
)

data class User(
    val activeState: UserActiveState = UserActiveState.UNAUTHENTICATED,
    val name: String? = null,
    val profileImage: String? = null,
    val generationList: SoptActiveGeneration? = null,
)

data class SoptActiveGeneration(
    private val value: List<Long>
) {
    fun isLongerOrEqual(comparator: Int): Boolean {
        return value.size >= comparator
    }

    val last get() = value.last()
    val isEmpty get() = value.isEmpty()
    val first get() = value.first()
    val length get() = value.size

    fun textAt(index: Int) = value[index].toString()
}

data class SoptActiveRecord(
    val attendanceScore: Double? = null,
    val announcement: String? = null,
)

data class HomeSection(
    val topDescription: String = "",
    val bottomDescription: String = ""
)
