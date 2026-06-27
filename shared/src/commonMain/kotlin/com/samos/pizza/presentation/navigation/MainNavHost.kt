package com.samos.pizza.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.samos.pizza.presentation.navigation.graph.mainNavGraph
import com.samos.pizza.presentation.navigation.router.ComposeRouter
import com.samos.pizza.presentation.navigation.router.ComposeRouterImpl
import com.samos.pizza.presentation.navigation.router.NavigationEffect
import com.samos.pizza.presentation.navigation.routes.NavigationRoute
import org.koin.compose.koinInject

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    router: ComposeRouter = koinInject()
) {
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current
    val implRouter = router as? ComposeRouterImpl

    DisposableEffect(navController) {
        implRouter?.attachNavController(navController)
        onDispose {
            implRouter?.detachNavController()
        }
    }

    LaunchedEffect(lifecycleOwner, navController) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            router.observe().collect { effect ->
                val nextRouteStr: String? = when (effect) {
                    is NavigationEffect.Navigate.Route -> effect.navRoute::class.qualifiedName
                    is NavigationEffect.Navigate.RouteBuilder -> effect.navRoute::class.qualifiedName
                    is NavigationEffect.Navigate.RoutePopUp -> effect.navRoute::class.qualifiedName
                    is NavigationEffect.NewChain -> effect.newChain.last()::class.qualifiedName
                    NavigationEffect.Back -> null
                    else -> null
                }
                if (nextRouteStr != null) implRouter?.setLastProcessedRouteStr(nextRouteStr)

                when (effect) {
                    NavigationEffect.Back -> {
                        val handled = implRouter?.handleCustomBack(navController) ?: false
                        if (handled) return@collect

                        val prevRouteStr = navController.previousBackStackEntry?.destination?.route
                        implRouter?.setLastProcessedRouteStr(prevRouteStr)

                        navController.navigateUp()
                    }

                    is NavigationEffect.Navigate.Route -> {
                        navController.navigate(effect.navRoute)
                    }

                    is NavigationEffect.Navigate.RouteBuilder -> {
                        navController.navigate(effect.navRoute, effect.builder)
                    }

                    is NavigationEffect.Navigate.RoutePopUp -> {
                        navController.navigate(effect.navRoute) {
                            // Get the current typed object or route for popUpTo
                            navController.currentBackStackEntry?.destination?.route?.let { currentRoute ->
                                popUpTo(currentRoute) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }

                    is NavigationEffect.NewChain -> {
                        navController.navigate(effect.newChain.last()) {
                            if (effect.isRoot) {
                                // Clearing the entire stack to its initial state in a Type-Safe manner
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.SplashRoute,
        modifier = modifier,
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = 100
                ),
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(durationMillis = 300),
            )
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = 100
                ),
            )
        },
        popExitTransition = {
            fadeOut(
                animationSpec = tween(durationMillis = 300),
            )
        },
        builder = { mainNavGraph() }
    )
}
