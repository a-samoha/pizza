package com.samos.pizza.presentation.screen.splash.viewmodel

import com.samos.pizza.presentation.base.MviIntent

sealed interface SplashIntent : MviIntent {

    object OnBackClick : SplashIntent
    object OnMinAnimSown : SplashIntent
}