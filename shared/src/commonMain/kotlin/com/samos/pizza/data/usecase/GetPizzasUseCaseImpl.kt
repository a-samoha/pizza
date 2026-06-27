package com.samos.pizza.data.usecase

import com.samos.pizza.data.repository.PizzasRepository
import com.samos.pizza.domain.model.PizzaModel
import com.samos.pizza.domain.usecase.GetPizzasUseCase
import kotlinx.coroutines.flow.Flow

class GetPizzasUseCaseImpl(
    private val repo: PizzasRepository,
) : GetPizzasUseCase {

    override fun invoke(fetchFromBe: Boolean): Flow<List<PizzaModel>> = repo.getPizzas(fetchFromBe)
}