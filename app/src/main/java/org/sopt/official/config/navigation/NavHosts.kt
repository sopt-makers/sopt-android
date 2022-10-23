package org.sopt.official.config.navigation

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@NavGraph
annotation class SplashNavGraph(
    val start: Boolean = false
)

@RootNavGraph
@NavGraph
annotation class AuthNavGraph(
    val start: Boolean = false
)

@RootNavGraph
@NavGraph
annotation class HomeNavGraph(
    val start: Boolean = false
)
