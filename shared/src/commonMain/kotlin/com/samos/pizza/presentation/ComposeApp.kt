package com.samos.pizza.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.samos.pizza.presentation.navigation.MainNavHost

@Composable
@Preview
fun ComposeApp() {
    MaterialTheme {
        MainNavHost()
    }
}
