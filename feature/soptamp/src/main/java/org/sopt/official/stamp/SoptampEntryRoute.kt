package org.sopt.official.stamp

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.IntentCompat
import androidx.navigation.NavController
import kotlinx.coroutines.flow.firstOrNull
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.compose.ProvideTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.stamp.feature.navigation.MissionDetail
import org.sopt.official.stamp.feature.navigation.PartRanking
import org.sopt.official.stamp.feature.navigation.Ranking
import org.sopt.official.stamp.feature.navigation.SoptampMissionArgs
import org.sopt.official.stamp.feature.navigation.SoptampRoute
import org.sopt.official.stamp.feature.navigation.UserMissionList
import timber.log.Timber

@Composable
fun SoptampEntryRoute(
    navController: NavController,
    tracker: Tracker,
    currentIntent: Intent?,
    content: @Composable () -> Unit
) {
    val args = remember(currentIntent) {
        currentIntent?.let { intent ->
            Timber.d("DeepLink: Intent Received? ${intent}, HasExtra? ${intent.hasExtra("soptampArgs")}")
            IntentCompat.getSerializableExtra(intent, "soptampArgs", SoptampMissionArgs::class.java)
        }
    }

    val deepLinkDestination: SoptampRoute? = remember(args) {
        if (args?.missionId == -1 || args == null) {
            null
        } else {
            MissionDetail(
                id = args.missionId,
                level = args.level,
                isCompleted = true,
                isMe = args.isMine,
                nickname = args.nickname.orEmpty(),
                title = args.title.orEmpty(),
            )
        }
    }

    SoptTheme {
        ProvideTracker(tracker) {
            val isDeepLinkHandled = remember { mutableStateOf(false) }

            LaunchedEffect(deepLinkDestination) {
                if (deepLinkDestination != null) {
                    isDeepLinkHandled.value = false
                }
            }

            LaunchedEffect(deepLinkDestination) {
                if (deepLinkDestination != null && !isDeepLinkHandled.value) {
                    isDeepLinkHandled.value = true

                    val destinationsToBuild = listOf(
                        PartRanking,
                        Ranking(
                            type = args?.part.orEmpty(),
                            entrySource = args?.entrySource ?: "personalRanking"
                        ),
                        UserMissionList(args?.nickname ?: "", "", args?.entrySource ?: ""),
                        deepLinkDestination
                    )

                    for (destination in destinationsToBuild) {
                        navController.navigate(destination)
                        navController.currentBackStackEntryFlow.firstOrNull {
                            it.destination.route?.startsWith(destination::class.qualifiedName!!) == true
                        }
                    }

                    currentIntent?.removeExtra("soptampArgs")
                }
            }

            content()
        }
    }
}