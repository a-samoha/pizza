package com.samos.pizza.presentation.navigation.router

import androidx.navigation.NavController
import com.samos.pizza.presentation.navigation.routes.NavigationRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock

/**
 * Type-Safe implementation of the [ComposeRouter] for Jetpack Compose Navigation in KMP
 * using Kotlin [Channel] and [Flow] to emit one-time navigation effects in a Jetpack Compose environment.
 *
 * This router allows decoupled navigation between composable screens without directly referencing
 * a [NavController]. Instead, it emits [NavigationEffect]s that can be collected and applied elsewhere.
 *
 * Typical usage:
 * ```
 * // In ViewModel
 * composeRouter.navigateTo(SomeRoute())
 *
 * // In Composable
 * LaunchedEffect(Unit) {
 *     composeRouter.observe().collect { effect ->
 *         applyNavigationEffect(effect, navController)
 *     }
 * }
 * ```
 *
 * @see ComposeRouter
 * @see NavigationEffect
 */
class ComposeRouterImpl : ComposeRouter {

    private val routerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    // use String (system route) as key instead of KClass
    private val chainBackStack = mutableMapOf<String, List<NavigationRoute>>()
    private var lastProcessedRouteStr: String? = null
    private var activeNavController: NavController? = null
    private var backStackObserverJob: Job? = null

    private var lastNavigationTime = 0L
    private val navigationThreshold = 350L

    private val _navEffect = MutableSharedFlow<NavigationEffect>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun observe(): SharedFlow<NavigationEffect> = _navEffect.asSharedFlow()

    override fun navigateBack() {
        if (!isNavigationAllowed()) return
        routerScope.launch { _navEffect.emit(NavigationEffect.Back) }
    }

    override fun backTo(route: NavigationRoute) {
        if (!isNavigationAllowed()) return
        routerScope.launch {
            _navEffect.emit(
                NavigationEffect.Navigate.RouteBuilder(navRoute = route) {
                    popUpTo(route) { inclusive = false }
                    launchSingleTop = true
                }
            )
        }
    }

    override fun replaceScreen(route: NavigationRoute) {
        if (!isNavigationAllowed()) return
        routerScope.launch {
            val currentRouteStr = activeNavController?.currentBackStackEntry?.destination?.route
            val targetRouteStr = route::class.qualifiedName ?: ""

            if (currentRouteStr != null) {
                chainBackStack[currentRouteStr]?.let { backStack ->
                    chainBackStack[targetRouteStr] = backStack
                }
                chainBackStack.remove(currentRouteStr)
            }
            _navEffect.emit(NavigationEffect.Navigate.RoutePopUp(navRoute = route))
        }
    }

    override fun navigateTo(route: NavigationRoute) {
        if (!isNavigationAllowed()) return
        routerScope.launch {
            _navEffect.emit(NavigationEffect.Navigate.Route(navRoute = route))
        }
    }

    override fun newRootScreen(route: NavigationRoute) {
        if (!isNavigationAllowed()) return
        routerScope.launch {
            _navEffect.emit(
                NavigationEffect.Navigate.RouteBuilder(navRoute = route) {
                    popUpTo(activeNavController?.graph?.startDestinationId ?: 0) { inclusive = true }
                    launchSingleTop = true
                }
            )
        }
    }

    override fun newRootChain(vararg routes: NavigationRoute) {
        if (!isNavigationAllowed()) return
        routerScope.launch {
            val targetRoute = routes.lastOrNull()
            if (targetRoute != null) {
                val targetRouteStr = targetRoute::class.qualifiedName ?: ""
                chainBackStack[targetRouteStr] = routes.dropLast(1)
            }
            _navEffect.emit(
                NavigationEffect.NewChain(newChain = routes.asList(), isRoot = true)
            )
        }
    }

    override fun newChain(vararg routes: NavigationRoute) {
        if (!isNavigationAllowed()) return
        routerScope.launch {
            val targetRoute = routes.lastOrNull()
            if (targetRoute != null) {
                val targetRouteStr = targetRoute::class.qualifiedName ?: ""
                chainBackStack[targetRouteStr] = routes.dropLast(1)
            }
            _navEffect.emit(
                NavigationEffect.NewChain(newChain = routes.asList(), isRoot = false)
            )
        }
    }

    fun attachNavController(navController: NavController) {
        this.activeNavController = navController
        backStackObserverJob?.cancel()

        backStackObserverJob = routerScope.launch {
            navController.currentBackStackEntryFlow.collect { entry ->
                // Використовуємо вбудований системний route
                val routeStr = entry.destination.route ?: return@collect

                if (routeStr != lastProcessedRouteStr) {
                    val prevRouteStr = navController.previousBackStackEntry?.destination?.route

                    if (prevRouteStr != null && chainBackStack.containsKey(prevRouteStr)) {
                        chainBackStack[routeStr] = chainBackStack[prevRouteStr].orEmpty()
                    }
                    lastProcessedRouteStr = routeStr
                }
            }
        }
    }

    fun detachNavController() {
        backStackObserverJob?.cancel()
        activeNavController = null
    }

    fun setLastProcessedRouteStr(routeStr: String?) {
        lastProcessedRouteStr = routeStr
    }

    fun handleCustomBack(navController: NavController): Boolean {
        return false
    }

    private fun isNavigationAllowed(): Boolean {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - lastNavigationTime > navigationThreshold) {
            lastNavigationTime = currentTime
            return true
        }
        return false
    }
}
