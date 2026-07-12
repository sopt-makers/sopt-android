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

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
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

@Serializable
data object SopletterGraph

@Serializable
data object SopletterEntry

fun NavController.navigateToSopletter(navOptions: NavOptions? = null) {
    navigate(SopletterGraph, navOptions)
}

fun NavGraphBuilder.sopletterGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    userStatus: UserStatus,
    navigateToHome: () -> Unit,
) {
    val viewType = userStatus.toViewType()
    navigation<SopletterGraph>(
        startDestination = SopletterEntry
    ) {
        composable<SopletterEntry> {
            SopletterEntryRoute(
                viewType = viewType,
                navigateToNickname = navController::navigateToSopletterName,
                navigateToHome = navigateToHome,
                navigateUp = navController::navigateUp,
                navigateToTopic = navController::navigateToSopletterTopic,
                navigateToTopicDetail = { topicId -> navController.navigateToSopletterMain(topicId = topicId) },
                navigateToWrite = { topicId -> navController.navigateToSopletterWrite(topicId) },
                navigateToPrint = { topicId -> navController.navigateToSopletterPrint(topicId) },
            )
        }

        composable<SopletterOnboarding> {
            SopletterOnboardingRoute(
                viewType = viewType,
                navigateToNickname = navController::navigateToSopletterName,
                navigateToHome = navigateToHome,
            )
        }

        composable<SopletterName> {
            SopletterNameRoute(
                viewType = viewType,
                navigateToSopletterMain = {
                    navController.navigateToSopletterMain(
                        navOptions = navOptions {
                            launchSingleTop = true
                            popUpTo<SopletterOnboarding> {
                                inclusive = true
                            }
                        },
                    )
                },
                navigateToHome = navigateToHome,
            )
        }

        composable<SopletterMain> {
            SopletterMainRoute(
                viewType = viewType,
                navigateUp = navController::navigateUp,
                navigateToTopic = navController::navigateToSopletterTopic,
                navigateToTopicDetail = { topicId -> navController.navigateToSopletterMain(topicId = topicId) },
                navigateToWrite = { topicId ->
                   navController.navigateToSopletterWrite(topicId)
                 },
                navigateToPrint = { topicId ->
                    navController.navigateToSopletterPrint(topicId)
                }
            )
        }

        composable<SopletterTopic> {
            SopletterTopicRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToMain = { topicId ->
                    navController.navigateToSopletterMain(topicId = topicId)
                },
            )
        }

        composable<SopletterWrite> {
            SopletterWriteRoute(
                viewType = viewType,
                onBackClick = navController::navigateUp,
                onNavigateToMain = { navController.navigateToSopletterMain() }
            )
        }

        composable<SopletterPrint> {
            SopletterPrintRoute(
                viewType = viewType,
                onBackClick = navController::navigateUp
            )
        }
    }
}
