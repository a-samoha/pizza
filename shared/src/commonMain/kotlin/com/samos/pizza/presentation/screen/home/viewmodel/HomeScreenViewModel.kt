package com.samos.pizza.presentation.screen.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.samos.pizza.domain.usecase.GetPizzasUseCase
import com.samos.pizza.presentation.base.MviEffect
import com.samos.pizza.presentation.base.MviViewModel
import com.samos.pizza.presentation.navigation.router.ComposeRouter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeScreenViewModel(
    getPizzasUseCase: GetPizzasUseCase,
    private val router: ComposeRouter,
) : MviViewModel<HomeState, HomeIntent, MviEffect>(HomeState()) {

    init {
        getPizzasUseCase(fetchFromBe = false)
            .onEach { pizzas ->
                pizzas?.let {
                    updateState { it.copy(pizzas = pizzas) }
                }
            }.launchIn(viewModelScope)
    }

    override fun handleIntent(intent: HomeIntent) =
        when (intent) {
            HomeIntent.OnBackClick -> router.navigateBack()
            is HomeIntent.OnSwipe -> {
                println("Test currentIndex=${intent.index}")
                updateState { it.copy(currentIndex = intent.index) }
            }
        }
}