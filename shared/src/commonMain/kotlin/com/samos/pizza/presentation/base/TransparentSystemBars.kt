package com.samos.pizza.presentation.base

import androidx.compose.runtime.Composable

/**
 * Multiplatform expect composable to dynamically toggle 100% transparent system bars
 * on Android and handle native layers on iOS.
 */
@Composable
expect fun TransparentSystemBarsEffect()