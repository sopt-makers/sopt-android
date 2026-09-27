package org.sopt.official.feature.poke.v2.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.sopt.official.feature.poke.v2.component.PokeScaffold
import org.sopt.official.feature.poke.v2.component.PokeSnackBarVisuals
import org.sopt.official.feature.poke.v2.friend.navigation.PokeFriend
import org.sopt.official.feature.poke.v2.main.navigation.PokeMain
import org.sopt.official.feature.poke.v2.onboarding.navigation.PokeOnboarding

@Serializable
data object PokeGraph

fun NavController.navigateToPoke(navOptions: NavOptions? = null) {
    navigate(PokeGraph, navOptions)
}

fun NavGraphBuilder.pokeGraph(
    navigateUp: () -> Unit,
) {
    composable<PokeGraph> {
        PokeNavHost(navigateUp = navigateUp)
    }
}

@Composable
private fun PokeNavHost(
    navigateUp: () -> Unit,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val onShowSnackbar: (PokeSnackBarVisuals) -> Unit = { visuals ->
        scope.launch { snackbarHostState.showSnackbar(visuals) }
    }

    PokeScaffold(snackbarHostState = snackbarHostState) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PokeMain,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<PokeOnboarding> {
                // TODO PokeOnboardingRoute
            }
            composable<PokeMain> {
                // TODO PokeMainRoute
            }
            composable<PokeFriend> {
                // TODO PokeFriendRoute
            }
        }
    }
}
