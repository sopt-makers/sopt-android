/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.stamp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import kotlinx.coroutines.flow.firstOrNull
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.compose.ProvideTracker
import org.sopt.official.common.di.AppScope
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.common.di.SoptViewModelFactory
import org.sopt.official.stamp.feature.navigation.MissionDetail
import org.sopt.official.stamp.feature.navigation.MissionList
import org.sopt.official.stamp.feature.navigation.PartRanking
import org.sopt.official.stamp.feature.navigation.Ranking
import org.sopt.official.stamp.feature.navigation.SoptampRoute
import org.sopt.official.stamp.feature.navigation.UserMissionList
import org.sopt.official.stamp.feature.navigation.soptampNavGraph
import java.io.Serializable

@Deprecated("SoptampEntryRoute로 대체")
@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey(SoptampActivity::class)
class SoptampActivity @Inject constructor(
    private val viewModelFactory: SoptViewModelFactory,
    private val tracker: Tracker
) : AppCompatActivity() {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

    data class SoptampMissionArgs(
        val id: Int,
        val missionId: Int,
        val isMine: Boolean,
        val nickname: String?,
        val part: String?,
        val level: Int,
        val title: String? = null
    ) : Serializable

    private val args by serializableExtra(
        SoptampMissionArgs(
            id = -1,
            missionId = -1,
            isMine = false,
            nickname = null,
            part = null,
            level = 1,
            title = null
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val deepLinkDestination: SoptampRoute? = if (args?.missionId == -1 || args == null) {
            null
        } else {
            MissionDetail(
                id = args?.missionId ?: -1,
                level = args?.level ?: 1,
                isCompleted = true,
                isMe = args?.isMine ?: false,
                nickname = args?.nickname.orEmpty(),
                title = args?.title.orEmpty(),
            )
        }

        setContent {
            CompositionLocalProvider(LocalMetroViewModelFactory provides viewModelFactory) {
                SoptTheme {
                    /*ProvideTracker(tracker) {
                        SoptampNavHost(
                            startDestination = MissionList,
                            deepLinkDestination = deepLinkDestination,
                            args = args
                        )
                    }*/
                }
            }
        }
    }

    companion object {
        fun getIntent(context: Context, args: SoptampMissionArgs? = null): Intent {
            return Intent(context, SoptampActivity::class.java).apply {
                putExtra("args", args)
            }
        }
    }
}

@Composable
private fun SoptampNavHost(
    startDestination: SoptampRoute,
    deepLinkDestination: SoptampRoute?,
    args: SoptampActivity.SoptampMissionArgs? = null
) {
    val navController = rememberNavController()

    // 딥링크 한 번만 처리하도록
    var isDeepLinkHandled by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        /*soptampNavGraph(
            navController = navController,
            onBackClick = { navController.popBackStack() },
        )*/
    }

    /*LaunchedEffect(deepLinkDestination, isDeepLinkHandled) {
        if (deepLinkDestination != null && deepLinkDestination != startDestination && !isDeepLinkHandled) {
            isDeepLinkHandled = true

            val destinationsToBuild = listOf(
                PartRanking,
                Ranking(args?.part.orEmpty()),
                UserMissionList(args?.nickname ?: "", ""),
                deepLinkDestination
            )

            for (destination in destinationsToBuild) {
                navController.navigate(destination)
                navController.currentBackStackEntryFlow.firstOrNull {
                    it.destination.route?.startsWith(destination::class.qualifiedName!!) == true
                }
            }
        }
    }*/
}