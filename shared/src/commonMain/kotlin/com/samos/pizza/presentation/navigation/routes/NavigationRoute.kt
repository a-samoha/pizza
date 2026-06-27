package com.samos.pizza.presentation.navigation.routes

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * [NavigationRoute] is a sealed interface that represents a unique route in the navigation system.
 *
 * This abstraction is used to define destinations in a strongly typed and serializable way.
 *
 * ## Features
 * - Annotated with [Serializable] to allow encoding/decoding routes as JSON strings,
 *   which is useful for deep links, restoring state, or passing complex route data across Compose screens.
 * - Marked as [@Immutable] for Compose stability guarantees.
 *
 * ## Usage
 * Each screen in the navigation system should define its own `data class` or `object` implementing `NavigationRoute`.
 *
 * ### Example:
 * ```
 * @Serializable
 * data class DetailsScreenRoute(val itemId: String) : NavigationRoute
 *
 * // Navigation:
 * router.navigateTo(DetailsScreenRoute("123"))
 * ```
 *
 * ## Notes
 * - This system assumes that all route classes are serializable using `kotlinx.serialization`.
 *   Avoid using non-serializable properties in route data classes.
 */
@Immutable
@Serializable
sealed interface NavigationRoute {

    @Serializable
    object SplashRoute : NavigationRoute

    @Serializable
    object HomeRoute : NavigationRoute

    @Serializable
    object SearchRoute : NavigationRoute

    @Serializable
    object SettingsRoute : NavigationRoute
}
