package com.samos.pizza

import androidx.compose.ui.window.ComposeUIViewController
import com.samos.pizza.di.initKoin
import com.samos.pizza.presentation.ComposeApp

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { ComposeApp() }