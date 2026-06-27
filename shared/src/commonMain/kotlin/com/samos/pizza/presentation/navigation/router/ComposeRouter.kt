package com.samos.pizza.presentation.navigation.router

import com.samos.pizza.presentation.navigation.routes.NavigationRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Interface for handling navigation actions in a Jetpack Compose application.
 *
 * `ComposeRouter` defines a set of high-level navigation methods that can be
 * used by view models or UI layers to perform navigation without directly depending
 * on navigation framework implementations.
 *
 * This abstraction allows for a clean separation of navigation logic and simplifies testing.
 * Navigation actions are emitted as [NavigationEffect]s which are observed and consumed
 * by the navigation host (e.g., in a `ComposeFragment` or `MainActivity`).
 */
interface ComposeRouter {

    /**
     * Returns a [Flow] of [NavigationEffect]s that should be observed by the navigation host.
     *
     * @return Flow emitting navigation effects.
     */
    fun observe(): SharedFlow<NavigationEffect>

    /**
     * Navigates back to the previous screen.
     * Equivalent to popping the current screen from the back stack.
     */
    fun navigateBack()

    /**
     * Navigates back to a specific screen, removing all intermediate destinations.
     *
     * @param route The target [NavigationRoute] to return to.
     */
    fun backTo(route: NavigationRoute)

    /**
     * Replaces the current screen with a new one.
     *
     * @param route The [NavigationRoute] representing the new screen.
     */
    fun replaceScreen(route: NavigationRoute)

    /**
     * Navigates to a new screen by pushing it onto the back stack.
     *
     * @param route The [NavigationRoute] representing the destination.
     */
    fun navigateTo(route: NavigationRoute)

    /**
     * Replaces the current back stack with a new root screen.
     *
     * @param route The [NavigationRoute] to set as the new root.
     */
    fun newRootScreen(route: NavigationRoute)

    /**
     * Clears the back stack and starts a new navigation chain from the given root.
     *
     * @param routes A variable number of [NavigationRoute]s, the first one being the new root.
     */
    fun newRootChain(vararg routes: NavigationRoute)

    /**
     * Replaces the current back stack with a new chain of screens.
     *
     * @param routes A variable number of [NavigationRoute]s forming the new navigation chain.
     */
    fun newChain(vararg routes: NavigationRoute)
}
