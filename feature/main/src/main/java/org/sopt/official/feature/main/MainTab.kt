package org.sopt.official.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.sopt.official.core.navigation.MainTabRoute
import org.sopt.official.core.navigation.Route

enum class MainTab(
    val icon: ImageVector,
    internal val contentDescription: String,
    val route: MainTabRoute,
) {
    Home(
        icon = Icons.Default.Home,
        contentDescription = "홈",
        route = org.sopt.official.feature.home.navigation.Home
    ),
    SoptLog(
        icon = Icons.Default.Person,
        contentDescription = "솝트로그",
        route = org.sopt.official.feature.soptlog.navigation.SoptLog
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
    }
}
