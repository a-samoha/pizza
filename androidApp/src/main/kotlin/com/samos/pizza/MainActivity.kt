package com.samos.pizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.samos.pizza.presentation.ComposeApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { false }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            ComposeApp()
        }
    }
}

@Preview
@Composable
fun ComposeAppAndroidPreview() {
    ComposeApp()
}
