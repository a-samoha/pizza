package com.samos.pizza.data.repository

import com.samos.pizza.data.source.network.OurSongApi
import com.samos.pizza.domain.model.PizzaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class PizzasRepository(
    private val api: OurSongApi,
) {

    private val _pizzas = MutableStateFlow<List<PizzaModel>?>(null)
    val pizzas = _pizzas.asStateFlow()

    fun getPizzas(fetchFromBe: Boolean = true): Flow<List<PizzaModel>?> = flow {
        if (fetchFromBe || _pizzas.value.isNullOrEmpty()) {
            val response = api.getPizzas()
            emit(response.pizzas.map { it.toDomain() })
        } else {
            emit(pizzas.value)
        }
    }
}
