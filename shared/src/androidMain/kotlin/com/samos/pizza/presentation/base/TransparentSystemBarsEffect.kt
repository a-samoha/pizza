package com.samos.pizza.presentation.base

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView

@Composable
actual fun TransparentSystemBarsEffect() {
    val view = LocalView.current
    val activity = view.context as? ComponentActivity

    DisposableEffect(activity) {
        // Forces 100% transparency for Android Status and Navigation bars upon entering the screen
        activity?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        onDispose {
            // Resets both bars to the dynamic platform default configuration when leaving the screen
            activity?.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT
                ),
                navigationBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT
                )
            )
        }
    }
}