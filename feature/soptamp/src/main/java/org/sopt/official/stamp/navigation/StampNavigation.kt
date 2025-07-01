package org.sopt.official.stamp.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.MissionLevel

// Top-level destinations
@Serializable
object MissionListRoute

@Serializable
data class MissionDetailRoute(
    val missionId: Int,
    val missionTitle: String,
    val missionLevel: Int,
    val isCompleted: Boolean,
    val isMe: Boolean,
    val nickname: String
)

@Serializable
data class UserMissionListRoute(
    val nickname: String,
    val description: String
)

@Serializable
object OnboardingRoute

@Serializable
object PartRankingRoute

@Serializable
data class RankingRoute(
    val type: String // Can be generation (e.g., "34기") or part (e.g., "안드")
)

// Navigation Arg Objects corresponding to routes if they take complex objects
// For MissionDetailScreen
data class MissionNavArgs(
    val id: Int,
    val title: String,
    val level: MissionLevel,
    val isCompleted: Boolean = false,
    val isMe: Boolean = true,
    val nickname: String
) {
    companion object {
        val typeMap = mapOf(
            "missionId" to NavType.IntType,
            "missionTitle" to NavType.StringType,
            "missionLevel" to NavType.IntType,
            "isCompleted" to NavType.BoolType,
            "isMe" to NavType.BoolType,
            "nickname" to NavType.StringType
        )
        val routeWithArgs =
            "mission/detail/{missionId}/{missionTitle}/{missionLevel}/{isCompleted}/{isMe}/{nickname}"
        val arguments = listOf(
            navArgument("missionId") { type = NavType.IntType },
            navArgument("missionTitle") { type = NavType.StringType },
            navArgument("missionLevel") { type = NavType.IntType },
            navArgument("isCompleted") { type = NavType.BoolType },
            navArgument("isMe") { type = NavType.BoolType },
            navArgument("nickname") { type = NavType.StringType }
        )

        fun from(missionDetailRoute: MissionDetailRoute) = MissionNavArgs(
            id = missionDetailRoute.missionId,
            title = missionDetailRoute.missionTitle,
            level = MissionLevel.of(missionDetailRoute.missionLevel),
            isCompleted = missionDetailRoute.isCompleted,
            isMe = missionDetailRoute.isMe,
            nickname = missionDetailRoute.nickname
        )
    }

    funtoMissionDetailRoute() = MissionDetailRoute(
        missionId = id,
        missionTitle = title,
        missionLevel = level.value,
        isCompleted = isCompleted,
        isMe = isMe,
        nickname = nickname
    )
}

// For UserMissionListScreen
data class RankerNavArg(
    val nickname: String,
    val description: String = ""
) {
    companion object {
        val typeMap = mapOf(
            "nickname" to NavType.StringType,
            "description" to NavType.StringType
        )
        val routeWithArgs = "mission/user/{nickname}/{description}"
        val arguments = listOf(
            navArgument("nickname") { type = NavType.StringType },
            navArgument("description") { type = NavType.StringType }
        )
        fun from(userMissionListRoute: UserMissionListRoute) = RankerNavArg(
            nickname = userMissionListRoute.nickname,
            description = userMissionListRoute.description
        )
    }
    fun toUserMissionListRoute() = UserMissionListRoute(
        nickname = nickname,
        description = description
    )
}

// For RankingScreen
data class RankingNavArg(
    val type: String
) {
    companion object {
        val typeMap = mapOf(
            "type" to NavType.StringType
        )
        val routeWithArgs = "ranking/{type}"
        val arguments = listOf(
            navArgument("type") { type = NavType.StringType }
        )
        fun from(rankingRoute: RankingRoute) = RankingNavArg(
            type = rankingRoute.type
        )
    }
    fun toRankingRoute() = RankingRoute(type = type)
}
