package com.samos.pizza.presentation.screen.home.viewmodel

import androidx.compose.runtime.Immutable
import com.samos.pizza.domain.model.PizzaModel
import com.samos.pizza.domain.model.PizzaModel.PizzaSize
import com.samos.pizza.presentation.base.MviState

@Immutable
data class HomeState(
    val pizzas: List<PizzaModel> = emptyList(),
    val currentIndex: Int = 0,
    val currentSize: PizzaSize = PizzaSize.M,
    val amount: Int = DEFAULT_AMOUNT,
) : MviState {

    companion object {
        const val DEFAULT_AMOUNT = 1
    }
}
