package core.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink

open class Destination(
    open val route: String,
    open val content: @Composable (NavController, NavBackStackEntry) -> Unit,
    open val deepLinks: List<NavDeepLink> = emptyList(),
    @Suppress("ktlint:standard:max-line-length")
    @JvmSuppressWildcards()
    open val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        fadeIn(animationSpec = tween())
    },
    @Suppress("ktlint:standard:max-line-length")
    open val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        fadeOut(animationSpec = tween())
    },
    open val arguments: List<NamedNavArgument> = emptyList(),
)
