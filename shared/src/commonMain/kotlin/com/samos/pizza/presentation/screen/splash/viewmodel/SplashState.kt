package com.samos.pizza.presentation.screen.splash.viewmodel

import com.samos.pizza.presentation.base.MviState

data class SplashState(
    val isLoading: Boolean = true,
) : MviState
