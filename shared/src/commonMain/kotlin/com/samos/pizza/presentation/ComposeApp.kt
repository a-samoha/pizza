package com.samos.pizza.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.samos.pizza.presentation.navigation.MainNavHost
import com.samos.pizza.presentation.theme.AppTheme

@Composable
@Preview
fun ComposeApp() {
    AppTheme {
        MainNavHost()
    }
}
