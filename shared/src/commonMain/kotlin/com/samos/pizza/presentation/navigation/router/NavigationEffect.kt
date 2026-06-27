package com.samos.pizza.presentation.navigation.router

import androidx.navigation.NavOptionsBuilder
import com.samos.pizza.presentation.navigation.routes.NavigationRoute

/**
 * Represents a one-time navigation action that can be emitted by [ComposeRouter]
 * and collected inside a navigation host to perform navigation.
 *
 * NavigationEffect is a sealed interface with different types of navigation operations,
 * including back navigation, forward navigation, and stack chain replacement.
 *
 * This abstraction decouples navigation commands from UI components or the NavController.
 */
interface NavigationEffect {

    /**
     * Navigation effect to perform a "back" operation, i.e., pop the current screen
     * from the back stack.
     */
    data object Back : NavigationEffect

    /**
     * Sealed interface for forward navigation commands, i.e., actions that move
     * the user to a new screen or replace the current one.
     */
    sealed interface Navigate : NavigationEffect {

        /** The destination route for this navigation effect. */
        val navRoute: NavigationRoute

        /**
         * Navigates to the specified [navRoute].
         *
         * @param navRoute the destination route to navigate to.
         */
        data class Route(
            override val navRoute: NavigationRoute
        ) : Navigate

        /**
         * Navigates to the specified [navRoute] using custom [builder] options.
         *
         * Useful for more advanced navigation like clearing parts of the back stack,
         * launching single top, or setting animations.
         *
         * @param navRoute the destination route.
         * @param builder lambda to configure navigation options such as popUpTo, inclusive, etc.
         */
        data class RouteBuilder(
            override val navRoute: NavigationRoute,
            val builder: NavOptionsBuilder.() -> Unit
        ) : Navigate

        /**
         * Replaces the current screen with the specified [navRoute] by popping it first.
         *
         * Effectively removes the current screen and pushes the new one.
         *
         * @param navRoute the destination screen to push after popping the current one.
         */
        data class RoutePopUp(
            override val navRoute: NavigationRoute
        ) : Navigate
    }

    /**
     * Navigation effect to replace the current navigation stack with a new chain of routes.
     *
     * Can optionally define the new chain as the root stack by setting [isRoot] to true.
     *
     * @param newChain the list of routes to set as the new stack.
     * @param isRoot whether the chain should become the new root (clearing all previous destinations).
     */
    data class NewChain(
        val newChain: List<NavigationRoute>,
        val isRoot: Boolean = false
    ) : NavigationEffect
}
