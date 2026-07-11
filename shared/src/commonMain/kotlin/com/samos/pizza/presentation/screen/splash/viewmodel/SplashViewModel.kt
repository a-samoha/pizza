package com.samos.pizza.presentation.screen.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.samos.pizza.domain.usecase.GetPizzasUseCase
import com.samos.pizza.presentation.base.MviEffect
import com.samos.pizza.presentation.base.MviViewModel
import com.samos.pizza.presentation.navigation.router.ComposeRouter
import com.samos.pizza.presentation.navigation.routes.NavigationRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SplashViewModel(
    getPizzasUseCase: GetPizzasUseCase,
    private val router: ComposeRouter,
) : MviViewModel<SplashState, SplashIntent, MviEffect>(SplashState()) {

    private var isMinAnimDone = false
    private var isLoaded = false

    init {
        getPizzasUseCase()
            .onEach { pizzas ->
                isLoaded = pizzas != null
                checkAndNavigate()
            }.launchIn(viewModelScope)
    }

    override fun handleIntent(intent: SplashIntent) = when (intent) {
        SplashIntent.OnBackClick -> Unit
        SplashIntent.OnMinAnimSown -> {
            isMinAnimDone = true
            checkAndNavigate()
        }
    }

    private fun checkAndNavigate() {
        if (isMinAnimDone && isLoaded)
            router.newRootScreen(NavigationRoute.HomeRoute)
    }
}