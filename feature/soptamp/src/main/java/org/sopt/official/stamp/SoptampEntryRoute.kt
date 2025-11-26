package org.sopt.official.stamp

import android.app.Activity
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val args = remember(activity) {
        val intent = activity?.intent
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getSerializableExtra("soptampArgs", SoptampMissionArgs::class.java)
            } else {
                intent?.getSerializableExtra("soptampArgs") as? SoptampMissionArgs
            }
        } catch (e: ClassCastException) {
            Timber.e(e)
            null
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
                if (deepLinkDestination != null && !isDeepLinkHandled.value) {
                    isDeepLinkHandled.value = true

                    val destinationsToBuild = listOf(
                        PartRanking,
                        Ranking(
                            type = "${args?.generation}기",
                            entrySource = args?.entrySource ?: "personalRanking"
                        ),
                        UserMissionList(args?.nickname ?: "", "", args?.entrySource ?: "personalRanking"),
                        deepLinkDestination
                    )

                    for (destination in destinationsToBuild) {
                        navController.navigate(destination)
                        navController.currentBackStackEntryFlow.firstOrNull {
                            it.destination.route?.startsWith(destination::class.qualifiedName!!) == true
                        }
                    }
                }
            }

            content()
        }
    }
}