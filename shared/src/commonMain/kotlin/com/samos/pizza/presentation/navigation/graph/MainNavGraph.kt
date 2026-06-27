package com.samos.pizza.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.samos.pizza.presentation.navigation.routes.NavigationRoute
import com.samos.pizza.presentation.screen.splash.SplashScreen

fun NavGraphBuilder.mainNavGraph() {
    composable<NavigationRoute.SplashRoute> {
        SplashScreen()
    }

    composable<NavigationRoute.HomeRoute> {
        //HomeScreen()
    }

    composable<NavigationRoute.SettingsRoute> {
        //SettingsScreen()
    }

    composable<NavigationRoute.SearchRoute> {
        //SearchScreen()
    }
}