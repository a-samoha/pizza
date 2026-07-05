package com.samos.pizza.presentation.screen.home.viewmodel

import androidx.compose.runtime.Immutable
import com.samos.pizza.domain.model.PizzaModel
import com.samos.pizza.presentation.base.MviState

@Immutable
data class HomeState(
    val pizzas: List<PizzaModel> = emptyList(),
    val currentIndex: Int = 0,
    val amount: Int = 1,
    val selectedSize: Int = 1,
) : MviState {
}
