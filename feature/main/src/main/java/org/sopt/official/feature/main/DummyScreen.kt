package org.sopt.official.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.sopt.official.core.navigation.MainTabRoute

@Composable
fun DummyScreen(
    paddingValues: PaddingValues,
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Dummy Screen")
    }
}

@Serializable
data object Dummy : MainTabRoute

fun NavController.navigateToDummy(
    navOptions: NavOptions? = null,
) {
    navigate(Dummy, navOptions)
}

fun NavGraphBuilder.dummyNavGraph(
    paddingValues: PaddingValues,
) {
    composable<Dummy> {
        DummyScreen(
            paddingValues = paddingValues
        )
    }
}

