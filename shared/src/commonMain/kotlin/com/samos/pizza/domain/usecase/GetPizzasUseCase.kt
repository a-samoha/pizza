package com.samos.pizza.domain.usecase

import com.samos.pizza.domain.model.PizzaModel
import kotlinx.coroutines.flow.Flow

interface GetPizzasUseCase {

    operator fun invoke(fetchFromBe: Boolean = true): Flow<List<PizzaModel>>
}
