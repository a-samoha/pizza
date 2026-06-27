package com.samos.pizza.data.repository

import com.samos.pizza.data.source.network.OurSongApi
import com.samos.pizza.domain.model.PizzaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PizzasRepository(
    private val api: OurSongApi,
) {

    fun getPizzas(fetchFromBe: Boolean = true): Flow<List<PizzaModel>> = flow {
        val result = api.getPizzas()
        emit(result.pizzas.map {
            it.toDomain()
        })
    }
}
