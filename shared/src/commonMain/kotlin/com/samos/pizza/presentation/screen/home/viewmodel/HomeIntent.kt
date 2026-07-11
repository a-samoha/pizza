package com.samos.pizza.presentation.screen.home.viewmodel

import com.samos.pizza.domain.model.PizzaModel.PizzaSize
import com.samos.pizza.presentation.base.MviIntent

sealed interface HomeIntent : MviIntent {

    object OnBackClick : HomeIntent
    data class OnSwipe(val index: Int) : HomeIntent
    data class OnSizeChanged(val size: PizzaSize) : HomeIntent
    data class OnAmountChanged(val isIncremented: Boolean) : HomeIntent
}