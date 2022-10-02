package org.sopt.official.feature.update

import com.google.android.play.core.install.model.AppUpdateType

enum class UpdateCriteria(
    val priority: Int,
    val stalenessDaysOfImmediateUpdate: Int?,
    val stalenessDaysOfFlexibleUpdate: Int?
) {
    URGENT(5, null, null),
    SLIGHTLY_URGENT(4, 5, 3),
    NORMAL(3, 30, 15),
    SLIGHTLY_FLEXIBLE(2, 90, 30),
    FLEXIBLE(1, null, null);

    companion object {
        private fun of(priority: Int): UpdateCriteria {
            return values().find { it.priority == priority }
                ?: throw IllegalStateException("Unknown priority: $priority")
        }

        private fun UpdateCriteria.stalenessDayOf(type: Int) =
            if (type == AppUpdateType.IMMEDIATE) {
                stalenessDaysOfImmediateUpdate!!
            } else {
                stalenessDaysOfFlexibleUpdate!!
            }

        fun isUpdatableOf(
            priority: Int, stalenessDays: Int?, updateType: Int
        ): Boolean {
            return when (priority) {
                5 -> true
                1 -> updateType == AppUpdateType.FLEXIBLE
                else -> {
                    if (stalenessDays == null) return false
                    val criteria = UpdateCriteria.of(priority)
                    stalenessDays >= criteria.stalenessDayOf(AppUpdateType.IMMEDIATE)
                }
            }
        }
    }
}
