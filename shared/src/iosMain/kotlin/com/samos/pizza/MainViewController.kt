package com.samos.pizza

import androidx.compose.ui.window.ComposeUIViewController
import com.samos.pizza.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { ComposeApp() }