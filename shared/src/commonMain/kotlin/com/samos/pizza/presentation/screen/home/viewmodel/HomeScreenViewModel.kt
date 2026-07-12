package com.samos.pizza.presentation.screen.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.samos.pizza.domain.usecase.GetPizzasUseCase
import com.samos.pizza.presentation.base.MviEffect
import com.samos.pizza.presentation.base.MviViewModel
import com.samos.pizza.presentation.navigation.router.ComposeRouter
import com.samos.pizza.presentation.screen.home.viewmodel.HomeState.Companion.DEFAULT_AMOUNT
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

    override fun handleIntent(intent: HomeIntent) {
        println("Test handleIntent $intent")

        when (intent) {
            HomeIntent.OnBackClick -> router.navigateBack()
            is HomeIntent.OnSwipe -> {
                updateState { it.copy(currentIndex = intent.index) }
            }
            is HomeIntent.OnSizeChanged -> {
                updateState { it.copy(currentSize = intent.size) }
            }
            is HomeIntent.OnAmountChanged -> {
                //updateState { it.copy(amount = if (intent.isIncremented) it.amount + 1 else if (it.amount > DEFAULT_AMOUNT) it.amount - 1 else DEFAULT_AMOUNT) }
                val delta = if (intent.isIncremented) 1 else -1
                updateState { it.copy(amount = (it.amount + delta).coerceAtLeast(DEFAULT_AMOUNT)) }
            }
        }
    }

    override fun onCleared() {
        println("Test onCleared")
        super.onCleared()

    }
}