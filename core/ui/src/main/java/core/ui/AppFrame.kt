package core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import core.ui.theme.AppTheme
import org.koin.androidx.compose.KoinAndroidContext

@Composable
fun App(destinations: List<Destination>) {
    val navController = rememberNavController()

    AppTheme {
        KoinAndroidContext {
            Scaffold { padding ->
                NavHost(
                    modifier = Modifier
                        .padding(padding),
                    navController = navController,
                    startDestination = destinations.first().route
                ) {
                    // Add all defined destinations to the NavGraph
                    destinations.forEach { dest ->
                        composable(
                            route = dest.route,
                            content = { navBackStackEntry ->
                                dest.content(navController, navBackStackEntry)
                            },
                            deepLinks = dest.deepLinks,
                            arguments = dest.arguments,
                            enterTransition = dest.enterTransition,
                            exitTransition = dest.exitTransition
                        )
                    }
                }
            }
        }
    }
}
