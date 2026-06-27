package com.samos.pizza.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samos.pizza.domain.usecase.GetPizzasUseCase
import com.samos.pizza.presentation.navigation.router.ComposeRouter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SplashViewModel(
    getPizzasUseCase: GetPizzasUseCase,
    private val router: ComposeRouter,
) : ViewModel() {

    init {
        getPizzasUseCase()
            .onEach { println("Test response $it") }
            .launchIn(viewModelScope)
    }
}