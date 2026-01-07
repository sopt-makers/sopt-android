/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.main

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import org.sopt.official.core.navigation.MainTabRoute
import org.sopt.official.core.navigation.Route

enum class MainTab(
    @param:DrawableRes val icon: Int,
    internal val contentDescription: String,
    val route: MainTabRoute,
    val loggingName: String? = null,
    val deeplink: String? = null
) {
    Home(
        icon = R.drawable.ic_main_home_filled,
        contentDescription = "홈",
        route = org.sopt.official.feature.home.navigation.Home,
        loggingName = "navi_home",
        deeplink = "home"
    ),

    Soptamp(
        icon = R.drawable.ic_main_soptamp,
        contentDescription = "솝탬프",
        route = org.sopt.official.stamp.feature.navigation.SoptampGraph,
        loggingName = "navi_soptamp",
        deeplink = "soptamp"
    ),

    Appjamtamp(
        icon = R.drawable.ic_main_soptamp,
        contentDescription = "솝탬프",
        route = org.sopt.official.feature.appjamtamp.navigation.AppjamtampNavGraph,
        loggingName = null,
        deeplink = "appjamtamp"
    ),

    Poke(
        icon = R.drawable.ic_main_poke,
        contentDescription = "콕찌르기",
        route = org.sopt.official.feature.poke.navigation.PokeGraph,
        loggingName = "navi_poke",
        deeplink = "poke"
    ),

    SoptLog(
        icon = R.drawable.ic_main_soptlog,
        contentDescription = "솝트로그",
        route = org.sopt.official.feature.soptlog.navigation.SoptLog,
        loggingName = "navi_soptlog",
        deeplink = "soptlog"
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }

        fun getActiveTabs(activeServices: List<String>): List<MainTab> {
            val activeServices = entries.filter { tab ->
                tab.deeplink != null && activeServices.contains(tab.deeplink)
            }

            return listOf(Home) + activeServices + listOf(SoptLog)
        }
    }
}
