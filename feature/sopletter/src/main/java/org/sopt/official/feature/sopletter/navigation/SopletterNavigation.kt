/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.sopletter.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.sopt.official.feature.sopletter.common.component.SopletterScaffold
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarVisuals
import org.sopt.official.feature.sopletter.main.SopletterMainRoute
import org.sopt.official.feature.sopletter.main.navigation.SopletterMain
import org.sopt.official.feature.sopletter.main.navigation.navigateToSopletterMain
import org.sopt.official.feature.sopletter.name.SopletterNameRoute
import org.sopt.official.feature.sopletter.name.navigation.SopletterName
import org.sopt.official.feature.sopletter.name.navigation.navigateToSopletterName
import org.sopt.official.feature.sopletter.onboarding.SopletterOnboardingRoute
import org.sopt.official.feature.sopletter.onboarding.navigation.SopletterOnboarding
import org.sopt.official.feature.sopletter.print.SopletterPrintRoute
import org.sopt.official.feature.sopletter.print.navigation.SopletterPrint
import org.sopt.official.feature.sopletter.print.navigation.navigateToSopletterPrint
import org.sopt.official.feature.sopletter.topic.SopletterTopicRoute
import org.sopt.official.feature.sopletter.topic.navigation.SopletterTopic
import org.sopt.official.feature.sopletter.topic.navigation.navigateToSopletterTopic
import org.sopt.official.feature.sopletter.write.SopletterWriteRoute
import org.sopt.official.feature.sopletter.write.navigation.SopletterWrite
import org.sopt.official.feature.sopletter.write.navigation.navigateToSopletterWrite
import org.sopt.official.model.UserStatus
import org.sopt.official.model.toViewType

// 좋아요 푸시로 진입하면 topicId, messageId가 채워져 해당 메시지 상세 모달을 바로 연다.
@Serializable
data class SopletterGraph(
    val topicId: Long? = null,
    val messageId: Long? = null,
)

@Serializable
data object SopletterEntry

private inline fun <reified T : Any> singleTopNavOptions() = navOptions {
    popUpTo<T> { inclusive = true }
    launchSingleTop = true
}

// SopletterMain이 목록(topicId == null)과 주제 상세(topicId != null)를 겸하고 있어
// popUpTo<SopletterMain>으로는 목록만 남길 수 없으므로 직접 목록까지 pop한다.
private fun NavController.popBackStackToMainList() {
    while (true) {
        val entry = currentBackStackEntry ?: return
        val isMainList = entry.destination.hasRoute<SopletterMain>() &&
            entry.toRoute<SopletterMain>().topicId == null
        val isTopicList = entry.destination.hasRoute<SopletterTopic>()
        if (isMainList || isTopicList) return
        if (!popBackStack()) return
    }
}

fun NavController.navigateToSopletter(
    topicId: Long? = null,
    messageId: Long? = null,
    navOptions: NavOptions? = singleTopNavOptions<SopletterGraph>(),
) {
    navigate(SopletterGraph(topicId = topicId, messageId = messageId), navOptions)
}

fun NavGraphBuilder.sopletterGraph(
    userStatus: UserStatus,
    navigateToHome: () -> Unit,
) {
    composable<SopletterGraph> { backStackEntry ->
        val route = backStackEntry.toRoute<SopletterGraph>()
        SopletterNavHost(
            userStatus = userStatus,
            pushTopicId = route.topicId,
            pushMessageId = route.messageId,
            navigateToHome = navigateToHome,
        )
    }
}

@Composable
private fun SopletterNavHost(
    userStatus: UserStatus,
    pushTopicId: Long?,
    pushMessageId: Long?,
    navigateToHome: () -> Unit,
) {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val viewType = userStatus.toViewType()

    val onShowSnackbar: (SopletterSnackbarVisuals) -> Unit = { visuals ->
        scope.launch { snackBarHostState.showSnackbar(visuals) }
    }

    SopletterScaffold(snackbarHostState = snackBarHostState) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SopletterEntry,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<SopletterEntry> {
                SopletterEntryRoute(
                    navigateToTopic = {
                        navController.navigateToSopletterTopic(
                            navOptions = singleTopNavOptions<SopletterEntry>(),
                        )
                        // 온보딩 완료 유저가 좋아요 푸시로 들어온 경우 주제 목록 위에 메시지 상세를 올린다.
                        // 온보딩 미완료 유저는 navigateToOnboarding으로 가서 푸시 목적지가 무시된다.
                        if (pushTopicId != null && pushMessageId != null) {
                            navController.navigateToSopletterMain(
                                topicId = pushTopicId,
                                messageId = pushMessageId,
                            )
                        }
                    },
                    navigateToOnboarding = {
                        navController.navigate(SopletterOnboarding) {
                            popUpTo<SopletterEntry> { inclusive = true }
                        }
                    },
                )
            }

            composable<SopletterOnboarding> {
                SopletterOnboardingRoute(
                    viewType = viewType,
                    navigateToNickname = navController::navigateToSopletterName,
                    navigateToHome = navigateToHome,
                    onShowSnackbar = onShowSnackbar,
                )
            }

            composable<SopletterName> {
                SopletterNameRoute(
                    viewType = viewType,
                    navigateToSopletterTopic = {
                        navController.navigateToSopletterTopic(
                            navOptions = singleTopNavOptions<SopletterOnboarding>(),
                        )
                    },
                    navigateToHome = navigateToHome,
                )
            }

            composable<SopletterMain> {
                val isTopicDetail = it.toRoute<SopletterMain>().topicId != null
                val onNavigateUp: () -> Unit = if (isTopicDetail) {
                    { if (!navController.popBackStack()) navigateToHome() }
                } else {
                    navigateToHome
                }
                SopletterMainRoute(
                    viewType = viewType,
                    onShowSnackbar = onShowSnackbar,
                    navigateUp = onNavigateUp,
                    navigateToTopic = navController::navigateToSopletterTopic,
                    navigateToTopicDetail = { topicId -> navController.navigateToSopletterMain(topicId = topicId) },
                    navigateToWrite = { topicId -> navController.navigateToSopletterWrite(topicId) },
                    navigateToPrint = { topicId -> navController.navigateToSopletterPrint(topicId) },
                )
            }

            composable<SopletterTopic> {
                SopletterTopicRoute(
                    navigateUp = navigateToHome,
                    navigateToMain = { topicId -> navController.navigateToSopletterMain(topicId = topicId) },
                    navigateToWrite = navController::navigateToSopletterWrite,
                )
            }

            composable<SopletterWrite> {
                SopletterWriteRoute(
                    viewType = viewType,
                    onShowSnackbar = onShowSnackbar,
                    onBackClick = navController::navigateUp,
                    onNavigateToMain = { topicId ->
                        // 주제 목록까지 돌아간 뒤, 작성한 주제의 메인을 새 엔트리로 올려 목록을 갱신한다.
                        navController.popBackStackToMainList()
                        navController.navigateToSopletterMain(topicId = topicId)
                    },
                )
            }

            composable<SopletterPrint> {
                SopletterPrintRoute(
                    viewType = viewType,
                    onShowSnackbar = onShowSnackbar,
                    onBackClick = navController::navigateUp,
                )
            }
        }
    }
}
